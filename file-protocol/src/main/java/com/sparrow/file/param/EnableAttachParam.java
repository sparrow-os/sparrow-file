package com.sparrow.file.param;

import java.util.List;

public class EnableAttachParam {
    private String belongBusiness;
    private Long belongId;
    private List<AttachRemark> attachRemarks;

    public EnableAttachParam() {
    }

    public EnableAttachParam(String belongType, Long belongId) {
        this.belongBusiness = belongType;
        this.belongId = belongId;
    }

    public String getBelongBusiness() {
        return belongBusiness;
    }

    public void setBelongBusiness(String belongBusiness) {
        this.belongBusiness = belongBusiness;
    }

    public Long getBelongId() {
        return belongId;
    }

    public void setBelongId(Long belongId) {
        this.belongId = belongId;
    }


    public List<AttachRemark> getAttachRemarks() {
        return attachRemarks;
    }

    public void setAttachRemarks(List<AttachRemark> attachRemarks) {
        this.attachRemarks = attachRemarks;
    }
}
