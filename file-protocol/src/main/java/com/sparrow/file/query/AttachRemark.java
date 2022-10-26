package com.sparrow.file.query;

public class AttachRemark {
    private Long refId;
    private String fileId;
    private String remark;

    public AttachRemark(Long refId, String fileId, String remark) {
        this.refId = refId;
        this.fileId = fileId;
        this.remark = remark;
    }

    public AttachRemark(String fileId, String remark) {
        this(null, fileId, remark);
    }

    public String getFileId() {
        return fileId;
    }

    public String getRemark() {
        return remark;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }
}
