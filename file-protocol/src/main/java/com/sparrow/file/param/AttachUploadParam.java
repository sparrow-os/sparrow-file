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
