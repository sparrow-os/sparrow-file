package com.sparrow.file;

import com.sparrow.protocol.POJO;

public class UploadingProgress implements POJO {
    private Long contentLength;
    private Long readLength;
    private String fileUrl;
    private String clientFileName;
    private Long fileId;
    private String contentType;
    private String status;
    private String error;

    private String humanReadableContentLength;
    private String humanReadableReadLength;

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public Long getReadLength() {
        return readLength;
    }

    public void setReadLength(Long readLength) {
        this.readLength = readLength;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHumanReadableContentLength() {
        return humanReadableContentLength;
    }

    public void setHumanReadableContentLength(String humanReadableContentLength) {
        this.humanReadableContentLength = humanReadableContentLength;
    }

    public String getHumanReadableReadLength() {
        return humanReadableReadLength;
    }

    public void setHumanReadableReadLength(String humanReadableReadLength) {
        this.humanReadableReadLength = humanReadableReadLength;
    }
}
