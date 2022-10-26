package com.sparrow.file.dto;

public class AttachRefDTO extends AttachDTO {

    private Long refId;
    /**
     * 文件所属对象类别
     */
    private String belongType;
    /**
     * 文件所属对象id
     */
    private Long belongId;
    private String remarks;
    private Integer status;
    private Long refTime;
    private Long refUpdateTime;

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getBelongType() {
        return belongType;
    }

    public void setBelongType(String belongType) {
        this.belongType = belongType;
    }

    public Long getBelongId() {
        return belongId;
    }

    public void setBelongId(Long belongId) {
        this.belongId = belongId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRefTime() {
        return refTime;
    }

    public void setRefTime(Long refTime) {
        this.refTime = refTime;
    }

    public Long getRefUpdateTime() {
        return refUpdateTime;
    }

    public void setRefUpdateTime(Long refUpdateTime) {
        this.refUpdateTime = refUpdateTime;
    }
}
