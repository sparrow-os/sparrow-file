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
package com.sparrow.file.param;

import lombok.Data;

import java.util.UUID;

@Data
public class AttachUploadParam {
    /**
     * 文件上传生成的序列号
     */
    private String serialNumber;
    /**
     * 目录的配置key,即belong business
     */
    private String pathKey;
    private String editor;
    private Long createUserId;
    private String clientFileName;
    private String contentType;
    private Integer contentLength;
    private String base64Content;

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }

    public String getSerialNumber() {
        if (this.serialNumber == null) {
            return UUID.randomUUID().toString();
        }
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
