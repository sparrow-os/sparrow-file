package com.sparrow.file.query;

import java.util.List;

public class EnableAttachQueryDTO {
    private String belongBusiness;
    private Long belongId;
    private List<AttachRemark> attachRemarkList;

    public EnableAttachQueryDTO() {
    }

    public EnableAttachQueryDTO(String belongType, Long belongId, List<AttachRemark> attachRemarks) {
        this.belongBusiness = belongType;
        this.belongId = belongId;
        this.attachRemarkList = attachRemarks;
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

    public List<AttachRemark> getAttachRemarkList() {
        return attachRemarkList;
    }

    public void setAttachRemarkList(List<AttachRemark> attachRemarkList) {
        this.attachRemarkList = attachRemarkList;
    }
}
