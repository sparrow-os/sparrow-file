/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.sparrow.file.controller;

import com.sparrow.context.SessionContext;
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
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.utility.FileUtility;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

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
    public void deleteAttach(@PathVariable("fileId") Long fileId) throws BusinessException {
        this.attachService.deleteImageById(fileId);
    }


    @PostMapping("/base64-upload.json")
    public String saveImageContent(@RequestBody AttachUploadParam attachUploadParam) throws BusinessException {
        FileConfig fileConfig = this.fileConfigAssemble.assemble(attachUploadParam.getPathKey());
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(attachUploadParam.getClientFileName());
        attachUploadParam.setContentType(fileNameProperty.getContentType());
        String physicalUrl = this.getPhysicalFilePath(attachUploadParam, fileConfig);
        int fileLength = FileUtility.getInstance().generateImage(attachUploadParam.getBase64Content(), physicalUrl);
        attachUploadParam.setContentLength(fileLength);
        this.uploadPostProcessStrategy.uploadPostProcessing(physicalUrl, attachUploadParam, fileConfig);
        return this.pathUrlConverter.getWebUrlByPhysicalFileName(physicalUrl);
    }

    @PostMapping("/upload.json")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "pathType", required = false) String pathType) throws BusinessException {

        Asserts.isTrue(file == null, FileError.UPLOAD_FILE_NAME_NULL);
        String physicalUpload = this.sparrowConfig.getMvc().getPhysicalUpload();

        try {
            FileConfig fileConfig = this.fileConfigAssemble.assemble(pathType);
            // 业务级大小限制（按 pathKey 配置 attach_<key> 的第 2 段 length，单位 byte）。
            // 超过时复用 Spring 的 MaxUploadSizeExceededException，把该 pathKey 上限作为 maxUploadSize 传入，
            // 统一交给 FileControllerAdvice 兜底：Result code=12202、data=可读限制大小。
            if (file.getSize() > fileConfig.getLength()) {
                throw new MaxUploadSizeExceededException(fileConfig.getLength());
            }
            LoginUser loginUser = SessionContext.getLoginUser();
            AttachUploadParam attachUploadParam = new AttachUploadParam();
            attachUploadParam.setClientFileName(file.getOriginalFilename());
            attachUploadParam.setContentType(file.getContentType());
            attachUploadParam.setContentLength((int) file.getSize());
            attachUploadParam.setCreateUserId(loginUser.getUserId());
            attachUploadParam.setEditor("");
            attachUploadParam.setPathKey(pathType);
            String physicalFullPath = this.getPhysicalFilePath(attachUploadParam, fileConfig);
            file.transferTo(new File(physicalFullPath));
            this.uploadPostProcessStrategy.uploadPostProcessing(physicalUpload, attachUploadParam, fileConfig);
            return this.pathUrlConverter.getWebUrlByPhysicalFileName(physicalFullPath);

        } catch (IOException e) {
            log.error("upload file error", e);
            throw new BusinessException(FileError.UPLOAD_SERVICE_ERROR);
        }
    }

    private String getPhysicalFilePath(AttachUploadParam attachUploadParam, FileConfig fileConfig) throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        String physicalFullPath = null;
        if (fileConfig.isShuffle()) {
            AttachDTO attach = this.attachService.generateFileId(attachUploadParam);
            physicalFullPath = FileConfig.getShuffleImagePhysicalPath(attach, FileConstant.SIZE.ORIGIN);
        } else {
            physicalFullPath = fileConfig.getPhysicalFilePath(attachUploadParam, loginUser);
        }
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(physicalFullPath);
        File dir = new File(fileNameProperty.getDirectory());
        if (!dir.exists()) {
            boolean ok = dir.mkdirs();
            Asserts.isTrue(!ok, FileError.DIR_CREATE_ERROR);
        }
        return physicalFullPath;
    }
}
