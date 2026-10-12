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
package com.sparrow.file.api;

import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.param.EnableAttachParam;
import com.sparrow.protocol.BusinessException;

/**
 * 记录附件的引用关系，不作为删除依据，是否删除由上传者决定
 */
public interface AttachService extends Downloader {

    AttachDTO generateFileId(AttachUploadParam attachUpload);

    void deleteImageById(Long fileId) throws BusinessException;

    AttachDTO getAttach(Long fileId) throws BusinessException;

    void addDownLoadTimes(String id) throws BusinessException;

    void enableAttach(EnableAttachParam enableAttachParam) throws BusinessException;
}
