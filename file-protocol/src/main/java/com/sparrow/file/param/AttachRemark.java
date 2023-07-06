package com.sparrow.file.param;

public class AttachRemark {

    public AttachRemark(Long fileId, String remark) {
        this.fileId = fileId;
        this.remark = remark;
    }

    private Long fileId;
    private String remark;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
