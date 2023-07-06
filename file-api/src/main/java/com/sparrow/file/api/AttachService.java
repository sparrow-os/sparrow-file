package com.sparrow.file.api;

import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.param.EnableAttachParam;
import com.sparrow.file.param.ImageCropperParam;
import com.sparrow.protocol.BusinessException;

import java.io.IOException;

/**
 * 记录附件的引用关系，不作为删除依据，是否删除由上传者决定
 */
public interface AttachService extends Downloader {

    AttachDTO generateFileId(AttachUploadParam attachUpload);

    void deleteImageById(Long fileId) throws BusinessException;

    AttachDTO getAttach(Long fileId) throws BusinessException;

    void addDownLoadTimes(String id) throws BusinessException;

    void enableAttach(EnableAttachParam enableAttachParam) throws BusinessException;

    String imageCropper(ImageCropperParam imageCropperParam) throws IOException;
}
