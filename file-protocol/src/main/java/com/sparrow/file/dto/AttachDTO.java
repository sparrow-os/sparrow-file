package com.sparrow.file.dto;

/**
 * Created by harry on 17/10/11.
 */
public class AttachDTO {
    /**
     * 文件id
     */
    private String fileId;
    private String contentType;
    private Long contentLength;
    private Long downLoadTimes;
    private Integer readLevel = 0;
    private String clientFileName;
    private String createTime;
    private Long createUserId;
    private String url;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getDownLoadTimes() {
        return downLoadTimes;
    }

    public void setDownLoadTimes(Long downLoadTimes) {
        this.downLoadTimes = downLoadTimes;
    }

    public Integer getReadLevel() {
        return readLevel;
    }

    public void setReadLevel(Integer readLevel) {
        this.readLevel = readLevel;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }


    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isImage() {
        return this.contentType.split("/")[0].toLowerCase().equals("image");
    }
}

