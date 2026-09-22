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
package com.sparrow.file.assemble;

import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.UploadingProgress;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.enums.UploadDealType;
import com.sparrow.protocol.Size;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Named;

/**
 * #pathKey=path(路径)|length(长度单位byte)|type|big_size(-1表示不限制)|middle_size|small_size
 */
@Named
public class FileConfigAssemble {

    public FileConfig assemble(String key) {
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        WebConfigReader webConfigReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
        FileConfig fileConfig = new FileConfig();
        fileConfig.setKey(key);
        String config = configReader.getValue("attach_" + key);
        if (StringUtility.isNullOrEmpty(config)) {
            throw new RuntimeException("key attach_" + key + " not found");
        }
        String[] configArray = config.split("\\|");
        fileConfig.setPath(configArray[0]);
        fileConfig.setLength(Integer.parseInt(configArray[1]));
        fileConfig.setUploadPhysicalPath(webConfigReader.getPhysicalUpload());

        if (configArray.length <= 2) {
            return fileConfig;
        }
        fileConfig.setDeal(true);
        String uploadType = configArray[2];
        if ("3p".equals(uploadType)) {
            uploadType = "_3p";
        }
        fileConfig.setType(UploadDealType.valueOf(uploadType.toUpperCase()));
        switch (fileConfig.getType()) {
            case _3P:
                fileConfig.setSize(new Size(configArray[3]));
                fileConfig.setBigSize(new Size(configArray[3]));
                fileConfig.setMiddleSize(new Size(configArray[4]));
                fileConfig.setSmallSize(new Size(configArray[5]));
                break;
            case BREAK_UP:
            case THUMBNAIL:
                fileConfig.setSize(new Size(configArray[3]));
                fileConfig.setBigSize(new Size(configArray[3]));
            default:
        }
        return fileConfig;
    }

    public void assemble(UploadingProgress progress) {
        progress.setHumanReadableContentLength(FileUtility.getInstance().getHumanReadableFileLength(progress.getContentLength()));
        progress.setHumanReadableReadLength(FileUtility.getInstance().getHumanReadableFileLength(progress.getReadLength()));
    }
}
