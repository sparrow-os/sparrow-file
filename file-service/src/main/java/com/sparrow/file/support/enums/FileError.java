package com.sparrow.file.support.enums;

import com.sparrow.constant.SparrowModule;
import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;

public enum FileError implements ErrorSupport {
  IMAGE_EXTENSION_NOT_FOUND(true, "01", "[%s] image extension  not found ");

  private boolean system;
  private ModuleSupport module;
  private String code;
  private String message;

  FileError(boolean system, String code, String message) {
    this.system = system;
    this.message = message;
    this.module = SparrowModule.ATTACH;
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