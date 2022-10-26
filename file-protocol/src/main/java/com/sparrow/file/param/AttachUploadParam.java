package com.sparrow.file.param;

public class AttachUploadParam {

    /**
     * 文件上传生成的序列号
     */
    private String fileSerialNumber;
    /**
     * 目录的配置key,即belong business
     */
    private String pathKey;
    private String editor;
    private Long createUserId;
    private String clientFileName;
    private String contentType;
    private Long contentLength;

    public String getFileSerialNumber() {
        return fileSerialNumber;
    }

    public void setFileSerialNumber(String fileSerialNumber) {
        this.fileSerialNumber = fileSerialNumber;
    }

    public String getPathKey() {
        return pathKey;
    }

    public void setPathKey(String pathKey) {
        this.pathKey = pathKey;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isImage() {
        return this.contentType.split("/")[0].toLowerCase().equals("image");
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
