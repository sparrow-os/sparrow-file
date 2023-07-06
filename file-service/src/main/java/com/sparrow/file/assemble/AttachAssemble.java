package com.sparrow.file.assemble;

import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.po.Attach;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class AttachAssemble {

    public Attach assembleNewAttach(AttachUploadParam bo) {
        Attach po = new Attach();
        po.setSerialNumber(bo.getSerialNumber());
        po.setClientFileName(bo.getClientFileName());
        po.setCreateUserId(bo.getCreateUserId());
        po.setGmtCreate(System.currentTimeMillis());
        po.setDownloadTimes(0L);
        po.setContentLength(bo.getContentLength());
        po.setContentType(bo.getContentType());
        return po;
    }

    public List<AttachDTO> poList2dtoList(List<Attach> attachList) {
        List<AttachDTO> dtoList = new ArrayList<AttachDTO>();
        for (Attach attach : attachList) {
            dtoList.add(this.po2dto(attach));
        }
        return dtoList;
    }

    public AttachDTO po2dto(Attach attach) {
        AttachDTO dto = new AttachDTO();
        if (attach == null) {
            return dto;
        }
        dto.setClientFileName(attach.getClientFileName());
        dto.setCreateUserId(attach.getCreateUserId());
        dto.setContentLength(attach.getContentLength());
        dto.setContentType(attach.getContentType());
        dto.setContentType("");
        dto.setContentLength(0L);
        dto.setClientFileName("");
        dto.setCreateUserId(0L);
        dto.setUrl("");
        dto.setId(0L);
        dto.setWidth(0);
        dto.setHeight(0);
        dto.setBusinessType(0);
        dto.setBusinessId(0);
        dto.setDownloadTimes(0L);
        dto.setGmtCreate("");
        return dto;
    }
}
