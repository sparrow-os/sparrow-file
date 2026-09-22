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

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;
import com.sparrow.support.web.WebConfigReader;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class BreakUpPostProcessing implements UploadPostProcessing {

    private static Logger logger = LoggerFactory.getLogger(BreakUpPostProcessing.class);
    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {

        WebConfigReader webConfigReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);

        String logoWaterFile = webConfigReader.getPhysicalResource()
                + webConfigReader.getWaterMark();
        String breakup = physicalFullPath.replace(
            FileConstant.SIZE.ORIGIN, FileConstant.SIZE.BIG);
        try {
            ImageUtility.makeThumbnail(physicalFullPath,
                breakup, fileConfig
                    .getBigSize().getWidth(),
                fileConfig.getBigSize()
                    .getHeight(),
                logoWaterFile, false);
        } catch (Exception e) {
            logger.error("make thumbnail", e);
        }
    }
}
