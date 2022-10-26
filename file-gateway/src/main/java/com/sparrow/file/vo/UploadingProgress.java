package com.sparrow.file.vo;

import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.FileUtility;

public class UploadingProgress implements POJO {
    private FileUtility fileUtility = FileUtility.getInstance();
    private Long contentLength;
    private Long readLength;
    private String fileUrl;
    private String clientFileName;
    private Long fileId;
    private String contentType;
    private String status;
    private String error;

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

    public String getHumanReadableContentLength() {
        if (contentLength == null) {
            return Symbol.EMPTY;
        }
        return fileUtility.getHumanReadableFileLength(contentLength);
    }

    public String getHumanReadableReadLength() {
        if (readLength == null) {
            return Symbol.EMPTY;
        }
        return fileUtility.getHumanReadableFileLength(readLength);
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
}
