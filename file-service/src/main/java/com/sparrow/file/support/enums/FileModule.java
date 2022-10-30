package com.sparrow.file.support.enums;

import com.sparrow.protocol.ModuleSupport;

public class FileModule {
    public static final ModuleSupport ATTACH = new ModuleSupport() {
        @Override
        public String code() {
            return "21";
        }

        @Override
        public String name() {
            return "ATTACH";
        }
    };
    public static final ModuleSupport UPLOAD = new ModuleSupport() {
        @Override
        public String code() {
            return "22";
        }

        @Override
        public String name() {
            return "UPLOAD";
        }
    };
}
