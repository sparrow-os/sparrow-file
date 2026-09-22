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
package com.sparrow.file.post.processing;

import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.enums.UploadDealType;
import com.sparrow.file.param.AttachUploadParam;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class UploadPostProcessStrategy implements UploadPostProcessing {
    private static Logger logger = LoggerFactory.getLogger(UploadPostProcessStrategy.class);
    @Inject
    private BreakUpPostProcessing breakUpPostProcessing;
    @Inject
    private P3PostProcessing p3PostProcessing;
    @Inject
    private ThumbnailPostProcessing thumbnailPostProcessing;

    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUpload, FileConfig fileConfig) {
        UploadDealType uploadDealType = fileConfig.getType();
        logger.info("uploading post processing dealType {}", fileConfig.getType());
        switch (uploadDealType) {
            case _3P:
                this.p3PostProcessing.uploadPostProcessing(physicalFullPath, attachUpload, fileConfig);
                break;
            case THUMBNAIL:
                this.thumbnailPostProcessing.uploadPostProcessing(physicalFullPath, attachUpload, fileConfig);
                break;
            case BREAK_UP:
                this.breakUpPostProcessing.uploadPostProcessing(physicalFullPath, attachUpload, fileConfig);
                break;
        }
    }
}
