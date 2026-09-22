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
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class ThumbnailPostProcessing implements UploadPostProcessing {
    private static Logger logger = LoggerFactory.getLogger(ThumbnailPostProcessing.class);

    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {
        String bigPath = physicalFullPath.replace(
                FileConstant.SIZE.ORIGIN, FileConstant.SIZE.BIG);
        try {
            // 缩放并保存至大图 使其在前台能够正常显示
            ImageUtility.makeThumbnail(physicalFullPath,
                bigPath, fileConfig.getSize()
                    .getWidth(),
                fileConfig.getSize()
                    .getHeight(), null, true);
        } catch (Exception e) {
            logger.error("make thumbnail", e);
        }
    }
}
