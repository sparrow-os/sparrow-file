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
package com.sparrow.file.support.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;

public enum FileError implements ErrorSupport {
    IMAGE_EXTENSION_NOT_FOUND(false, FileModule.UPLOAD, "01", "[%s] image extension  not found "),
    UPLOAD_SERVICE_ERROR(false, FileModule.UPLOAD, "01", "upload service error"),
    UPLOAD_OUT_OF_SIZE(false, FileModule.UPLOAD, "02", "upload out of size"),
    UPLOAD_FILE_NAME_NULL(false, FileModule.UPLOAD, "03", "upload file name null"),
    UPLOAD_FILE_TYPE_ERROR(false, FileModule.UPLOAD, "04", "upload file type error"),
    FILE_NOT_FOUND(false, FileModule.UPLOAD, "31", "file not found"),
    FILE_CAN_NOT_READ(false, FileModule.UPLOAD, "32", "file can't read"),
    UPLOAD_SRC_DESC_PATH_REPEAT(false, FileModule.UPLOAD, "33", "src desc path repeat"),
    DIR_CREATE_ERROR(false, FileModule.UPLOAD, "34", "dir create error"),
    ;
    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    FileError(boolean system, ModuleSupport module, String code, String message) {
        this.system = system;
        this.message = message;
        this.module = module;
        this.code = (system ? 0 : 1) + module.code() + code;
    }

    @Override
    public boolean system() {
        return this.system;
    }

    @Override
    public ModuleSupport module() {
        return this.module;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
