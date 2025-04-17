package com.sparrow.file.support.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;
import com.sparrow.protocol.constant.GlobalModule;

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
