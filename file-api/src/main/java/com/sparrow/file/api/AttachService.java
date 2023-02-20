package com.sparrow.file.api;

import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.dto.AttachRefDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.query.AttachRemark;
import com.sparrow.protocol.BusinessException;
import java.util.List;

/**
 * 记录附件的引用关系，不作为删除依据，是否删除由上传者决定
 */
public interface AttachService extends Downloader {

    String generateFileId(AttachUploadParam attachUpload);

    void deleteAttach(String attachId) throws BusinessException;

    AttachDTO getAttach(String fileId) throws BusinessException;

    void addDownLoadTimes(String id) throws BusinessException;

    void modifyStatus(Long belongId, String business, List<AttachRemark> attachRemarks) throws BusinessException;
}
