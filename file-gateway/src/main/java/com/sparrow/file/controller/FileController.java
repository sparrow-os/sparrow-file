package com.sparrow.file.controller;

import com.sparrow.exception.Asserts;
import com.sparrow.file.api.AttachService;
import com.sparrow.file.assemble.FileConfigAssemble;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.post.processing.UploadPostProcessStrategy;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.enums.FileError;
import com.sparrow.file.support.utils.path.url.PathUrlConverter;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.spring.starter.config.SparrowConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
public class FileController {
    @Inject
    private AttachService attachService;

    @Inject
    private SparrowConfig sparrowConfig;

    @Inject
    private UploadPostProcessStrategy uploadPostProcessStrategy;

    @Inject
    private FileConfigAssemble fileConfigAssemble;

    @Inject
    private PathUrlConverter pathUrlConverter;


    @GetMapping("/delete-file/{fileId}")
    public void deleteAttach(Long fileId) throws BusinessException {
        this.attachService.deleteImageById(fileId);
    }


    @PostMapping("/upload.json")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             String pathType) throws BusinessException {

        Asserts.isTrue(file == null, FileError.UPLOAD_FILE_NAME_NULL);
        try {
            LoginUser loginUser = ThreadContext.getLoginToken();
            AttachUploadParam attachUploadParam = new AttachUploadParam();
            attachUploadParam.setClientFileName(file.getOriginalFilename());
            attachUploadParam.setContentType(file.getContentType());
            attachUploadParam.setContentLength(file.getSize());
            attachUploadParam.setCreateUserId(loginUser.getUserId());
            attachUploadParam.setEditor("im");

            String physicalUpload = this.sparrowConfig.getMvc().getPhysicalUpload();
            FileConfig fileConfig = this.fileConfigAssemble.assemble(pathType);
            String physicalFullPath = null;
            if (fileConfig.isShuffle()) {
                AttachDTO attach = this.attachService.generateFileId(attachUploadParam);
                physicalFullPath = FileConfig.getShuffleImagePhysicalPath(attach, FileConstant.SIZE.ORIGIN);
            } else {
                physicalFullPath = fileConfig.getPhysicalFilePath(attachUploadParam, loginUser);
            }
            File dir = new File(physicalFullPath);
            if (!dir.exists()) {
                boolean ok = dir.mkdirs();
                Asserts.isTrue(!ok, FileError.DIR_CREATE_ERROR);
            }
            file.transferTo(new File(physicalFullPath));
            this.uploadPostProcessStrategy.uploadPostProcessing(physicalUpload, attachUploadParam, fileConfig);
            return this.pathUrlConverter.getWebUrlByPhysicalFileName(physicalFullPath);
        } catch (IOException e) {
            log.error("upload file error", e);
            throw new BusinessException(FileError.UPLOAD_SERVICE_ERROR);
        }
    }
}
