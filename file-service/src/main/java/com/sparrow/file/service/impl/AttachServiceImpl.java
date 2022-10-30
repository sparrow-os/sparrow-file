package com.sparrow.file.service.impl;

import com.sparrow.constant.Config;
import com.sparrow.constant.File.SIZE;
import com.sparrow.file.api.AttachService;
import com.sparrow.file.assemble.AttachAssemble;
import com.sparrow.file.dao.AttachDAO;
import com.sparrow.file.dao.AttachRefDAO;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.dto.AttachRefDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.po.Attach;
import com.sparrow.file.po.AttachRef;
import com.sparrow.file.query.AttachRemark;
import com.sparrow.file.query.EnableAttachQueryDTO;
import com.sparrow.file.support.utils.ImageUtility;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Downloader;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.dao.UniqueKeyCriteria;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.HttpClient;
import com.sparrow.utility.StringUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sparrow.protocol.constant.SparrowError.GLOBAL_DB_LOAD_ERROR;

@Named("attachService")
public class AttachServiceImpl implements AttachService, Downloader {
    private static Logger logger = LoggerFactory.getLogger(AttachServiceImpl.class);
    @Inject
    private AttachAssemble attachAssemble;
    @Inject
    private AttachDAO attachDao;
    @Inject
    private AttachRefDAO attachRefDao;

    private FileUtility fileUtility = FileUtility.getInstance();

    @Override
    public AttachDTO getAttach(String fileId) throws BusinessException {
        try {
            UniqueKeyCriteria uniqueKeyCriteria = UniqueKeyCriteria.createUniqueCriteria(fileId, "fileId");
            return this.attachAssemble.po2dto(this.attachDao.getEntityByUnique(uniqueKeyCriteria));
        } catch (Exception e) {
            logger.error("get attach", e);
            throw new BusinessException(GLOBAL_DB_LOAD_ERROR);
        }
    }

    @Override
    public String generateFileId(AttachUploadParam attachUpload) {
        Attach attach = this.attachAssemble.assembleNewAttach(attachUpload);
        attachDao.insert(attach);
        return attach.getFileId();
    }

    public void deleteAttach(String attachId) throws BusinessException {
        try {
            UniqueKeyCriteria uniqueKeyCriteria = UniqueKeyCriteria.createUniqueCriteria(attachId, "file_id");
            Attach attach = this.attachDao.getEntityByUnique(uniqueKeyCriteria);
            if (attach != null) {
                fileUtility.deleteByFileId(attachId, attach.getClientFileName());
                this.attachDao.delete(attach.getId());
            }
        } catch (Exception e) {
            logger.error("delete attach", e);
            throw new BusinessException(SparrowError.GLOBAL_DB_DELETE_ERROR);
        }
    }

    @Override
    public List<AttachRefDTO> getEnableAttachList(Long belongId, String belongType) throws BusinessException {
        try {
            List<AttachRef> attachRefs = this.attachRefDao.getEnableAttachList(belongId, belongType);
            List<Long> attachIdList = attachRefs.stream().map(AttachRef::getId).collect(Collectors.toList());
            Map<Long, Attach> attacheMap = this.attachDao.getAttachMap(attachIdList);
            return this.attachAssemble.po2dtoList(attacheMap, attachRefs);
        } catch (Exception e) {
            logger.error("get attach list", e);
            throw new BusinessException(GLOBAL_DB_LOAD_ERROR);
        }
    }

    @Override
    public void addDownLoadTimes(String fileId) throws BusinessException {
        try {
            this.attachDao.addDownLoadTimes(fileId);
        } catch (Exception e) {
            logger.error("add attach download times", e);
            throw new BusinessException(SparrowError.GLOBAL_DB_ADD_ERROR);
        }
    }

    @Override
    public String downloadImage(String imageUrl, Long authorId) throws BusinessException {
        Attach attach = new Attach(imageUrl, "image/*", authorId);
        this.attachDao.insert(attach);

        String extension = this.fileUtility.getFileNameProperty(
            attach.getClientFileName()).getExtension();
        if (StringUtility.isNullOrEmpty(extension)) {
            logger.warn("image url is wrong {}", imageUrl);
            extension = Extension.PNG;
        }
        String originImagePath = this.fileUtility.getShufflePath(
            attach.getFileId(), extension, false,
            SIZE.ORIGIN);
        // 保存下载的原图
        HttpClient.downloadFile(imageUrl, originImagePath);

        File originImage = new File(originImagePath);
        attach.setContentLength(originImage.length());
        String bigPath = originImagePath
            .replace(SIZE.ORIGIN, SIZE.BIG);
        String middlePath = originImagePath.replace(SIZE.ORIGIN,
            SIZE.MIDDLE);
        String smallPath = originImagePath.replace(SIZE.ORIGIN,
            SIZE.SMALL);

        try {
            String logoWaterFile = ConfigUtility
                .getValue(Config.RESOURCE_PHYSICAL_PATH)
                + "/system/images/water.png";
            ImageUtility.makeThumbnail(originImagePath, bigPath, 480, -1,
                logoWaterFile, false);
            ImageUtility.makeThumbnail(originImagePath, middlePath, 240, -1, null,
                false);
            ImageUtility.makeThumbnail(originImagePath, smallPath, 180, 135, null,
                true);

            //更新图片的大小
            this.attachDao.update(attach);
            return attach.getFileId();
        } catch (Exception e) {
            logger.error("update attach", e);
            throw new BusinessException(SparrowError.GLOBAL_DB_UPDATE_ERROR);
        }
    }

    /**
     * modify status
     * <p>
     * 附件(包括图片)要求只有作者可以引用，其他人不允许引用 修改逻辑 1. 先获取当前id关联的所有原始附件 2. 将所有原始附件disable 3. 将原有图片，仍引用部分enable 4. 将不存在的附件新增 g.e ...
     * origin: 1,2,3,4 modify: 1,2,5,6 1. disable all 2. enable old 1,2 3. add new 5,6
     *
     * @param belongId      所属业务id
     * @param belongType    所属业务类型
     * @param attachRemarks 附件备注信息
     */
    @Override
    public void modifyStatus(Long belongId, String belongType,
        List<AttachRemark> attachRemarks) throws BusinessException {
        try {
            List<AttachRef> originAttachList = this.attachRefDao.getAttachList(belongId, belongType);
            List<AttachRemark> enableAttachRemark = new ArrayList<>();
            List<AttachRemark> newAttachRemark = new ArrayList<>();
            Set<Long> originRefSet = new HashSet<>();
            for (AttachRef ref : originAttachList) {
                originRefSet.add(ref.getId());
            }
            if (!CollectionsUtility.isNullOrEmpty(attachRemarks)) {
                for (AttachRemark attachRemark : attachRemarks) {
                    if (originRefSet.contains(attachRemark.getRefId())) {
                        enableAttachRemark.add(attachRemark);
                    } else {
                        newAttachRemark.add(attachRemark);
                    }
                }
            }
            //validate new attach is found
            if (!CollectionsUtility.isNullOrEmpty(newAttachRemark)) {
                for (AttachRemark attachRemark : newAttachRemark) {
                    UniqueKeyCriteria uniqueKeyCriteria = UniqueKeyCriteria.createUniqueCriteria(attachRemark.getFileId(), "fileId");
                    if (this.attachDao.getCountByUnique(uniqueKeyCriteria) == 0) {
                        throw new RuntimeException("attach file '" + attachRemark.getFileId() + "'not found");
                    }
                }
            }
            //disable all origin
            if (!CollectionsUtility.isNullOrEmpty(originAttachList)) {
                this.attachRefDao.disable(belongType, belongId);
            }
            //enable old
            if (!CollectionsUtility.isNullOrEmpty(enableAttachRemark)) {
                this.attachRefDao.enable(new EnableAttachQueryDTO(belongType, belongId, enableAttachRemark));
            }
            //add new attach
            if (!CollectionsUtility.isNullOrEmpty(newAttachRemark)) {
                for (AttachRemark attachRemark : newAttachRemark) {
                    AttachRef attachRef = this.attachAssemble.attachRemark2Ref(belongId, belongType, attachRemark);
                    this.attachRefDao.insert(attachRef);
                }
            }
        } catch (Exception e) {
            logger.error("modify attach status error", e);
            throw new BusinessException(SparrowError.GLOBAL_DB_UPDATE_ERROR);
        }
    }
}
