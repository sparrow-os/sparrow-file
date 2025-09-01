package com.sparrow.file.service.impl;

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.api.AttachService;
import com.sparrow.file.assemble.AttachAssemble;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.dao.AttachDAO;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.param.EnableAttachParam;
import com.sparrow.file.param.ImageCropperParam;
import com.sparrow.file.po.Attach;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;
import com.sparrow.file.support.utils.path.url.PathUrlConverter;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.HttpClient;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Named("attachService")
@Slf4j
public class AttachServiceImpl implements AttachService {
    public AttachServiceImpl() {
        log.info("init AttachService");
    }

    @Inject
    private AttachAssemble attachAssemble;

    @Inject
    private PathUrlConverter pathUrlConverter;

    @Inject
    private AttachDAO attachDao;
    private FileUtility fileUtility = FileUtility.getInstance();

    @Override
    public AttachDTO generateFileId(AttachUploadParam attachUpload) {
        Attach attach = this.attachAssemble.assembleNewAttach(attachUpload);
        attachDao.insert(attach);
        return this.attachAssemble.po2dto(attach);
    }

    /**
     * 删除文件
     */
    public void deleteImageById(Long attachId) throws BusinessException {
        AttachDTO attachDTO = this.getAttach(attachId);
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(attachDTO.getClientFileName());
        Boolean isImage = fileNameProperty.getImage();
        boolean result;
        if (isImage) {
            String imageFullPath = new FileConfig().getShuffleImagePhysicalPath(attachDTO, FileConstant.SIZE.ORIGIN);
            File origin = new File(imageFullPath);
            if (origin.exists()) {
                result = origin.delete();
                log.info("deleted file {},result:{}", imageFullPath, result);
            }

            File big = new File(imageFullPath.replace(FileConstant.SIZE.ORIGIN,
                    FileConstant.SIZE.BIG));
            if (big.exists()) {
                result = big.delete();
                log.info("deleted file {},result:{}", big.getAbsolutePath(), result);
            }

            File middle = new File(imageFullPath.replace(FileConstant.SIZE.ORIGIN,
                    FileConstant.SIZE.MIDDLE));
            if (middle.exists()) {
                result = middle.delete();
                log.info("deleted file {},result:{}", middle.getAbsolutePath(), result);
            }

            File small = new File(imageFullPath.replace(FileConstant.SIZE.ORIGIN,
                    FileConstant.SIZE.SMALL));
            if (small.exists()) {
                result = small.delete();
                log.info("deleted file {},result:{}", imageFullPath, result);
            }
        }
    }

    @Override
    public AttachDTO getAttach(Long fileId) throws BusinessException {
        Attach attach = this.attachDao.getEntity(fileId);
        return this.attachAssemble.po2dto(attach);
    }

    @Override
    public void addDownLoadTimes(String fileId) throws BusinessException {
        try {
            this.attachDao.addDownLoadTimes(fileId);
        } catch (Exception e) {
            log.error("add attach download times", e);
            throw new BusinessException(SparrowError.GLOBAL_DB_ADD_ERROR);
        }
    }

    @Override
    public String downloadImage(String imageUrl, Long authorId) throws BusinessException {
        Attach attach = new Attach(imageUrl, "image/*", authorId);
        this.attachDao.insert(attach);

        AttachDTO attachDTO = this.attachAssemble.po2dto(attach);

        String extension = this.fileUtility.getFileNameProperty(
                attach.getClientFileName()).getExtension();
        if (StringUtility.isNullOrEmpty(extension)) {
            log.warn("image url is wrong {}", imageUrl);
            extension = Extension.PNG;
        }
        String originImagePath = FileConfig.getShuffleImagePhysicalPath(attachDTO, FileConstant.SIZE.ORIGIN);
        // 保存下载的原图
        HttpClient.downloadFile(imageUrl, originImagePath);

        File originImage = new File(originImagePath);
        attach.setContentLength(originImage.length());
        String bigPath = originImagePath
                .replace(FileConstant.SIZE.ORIGIN, FileConstant.SIZE.BIG);
        String middlePath = originImagePath.replace(FileConstant.SIZE.ORIGIN,
                FileConstant.SIZE.MIDDLE);
        String smallPath = originImagePath.replace(FileConstant.SIZE.ORIGIN,
                FileConstant.SIZE.SMALL);

        try {
            WebConfigReader webConfigReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
            String logoWaterFile = webConfigReader.getPhysicalResource()
                    + "/system/images/water.png";
            ImageUtility.makeThumbnail(originImagePath, bigPath, 480, -1,
                    logoWaterFile, false);
            ImageUtility.makeThumbnail(originImagePath, middlePath, 240, -1, null,
                    false);
            ImageUtility.makeThumbnail(originImagePath, smallPath, 180, 135, null,
                    true);

            //更新图片的大小
            this.attachDao.update(attach);
            return attach.getSerialNumber();
        } catch (Exception e) {
            log.error("update attach", e);
            throw new BusinessException(SparrowError.GLOBAL_DB_UPDATE_ERROR);
        }
    }

    /**
     * modify status
     * <p>
     * 附件(包括图片)要求只有作者可以引用，
     * <p>
     * 其他人不允许引用 修改逻辑
     * <p>
     * 1. 先获取当前id关联的所有原始附件
     * <p>
     * 2. 将所有原始附件disable
     * <p>
     * 3. 将原有图片，仍引用部分enable
     * <p>
     * 4. 将不存在的附件新增 g.e ...
     * <p>
     * origin: 1,2,3,4
     * <p>
     * modify: 1,2,5,6 1.
     * <p>
     * disable all
     * <p>
     * 2. enable old 1,2 3.
     * <p>
     * add new 5,6
     *
     * @param enableAttachParam
     */
    @Override
    public void enableAttach(EnableAttachParam enableAttachParam) throws BusinessException {

    }

    @Override
    public String imageCropper(ImageCropperParam imageCropperParam) throws IOException {
        Rectangle rectangle = new Rectangle(imageCropperParam.getX(), imageCropperParam.getY(),
                imageCropperParam.getWidth(), imageCropperParam.getHeight());

        String physicalFilePath = this.pathUrlConverter.getPhysicalFileByWebUrl(imageCropperParam.getImageUrl());
        String smallPhysicalFilePath = physicalFilePath.replace(FileConstant.SIZE.BIG, FileConstant.SIZE.SMALL);
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(smallPhysicalFilePath);
        File directory = new File(fileNameProperty.getDirectory());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        ImageUtility.saveSubImage(physicalFilePath, rectangle, smallPhysicalFilePath);
        return this.pathUrlConverter.getWebUrlByPhysicalFileName(smallPhysicalFilePath);
    }
}
