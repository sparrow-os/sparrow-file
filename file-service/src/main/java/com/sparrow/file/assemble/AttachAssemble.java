package com.sparrow.file.assemble;

import com.sparrow.constant.DateTime;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.dto.AttachRefDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.po.Attach;
import com.sparrow.file.po.AttachRef;
import com.sparrow.file.query.AttachRemark;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.DateTimeUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Named;

@Named
public class AttachAssemble {

    public Attach assembleNewAttach(AttachUploadParam bo) {
        Attach po = new Attach();
        po.setFileId(bo.getFileSerialNumber());
        po.setClientFileName(bo.getClientFileName());
        po.setCreateUserId(bo.getCreateUserId());
        po.setCreateTime(System.currentTimeMillis());
        po.setDownloadTimes(0L);
        po.setContentLength(bo.getContentLength());
        po.setContentType(bo.getContentType());
        po.setReadLevel(0);
        return po;
    }

    public List<AttachDTO> poList2dtoList(List<Attach> attachList) {
        List<AttachDTO> dtoList = new ArrayList<AttachDTO>();
        for (Attach attach : attachList) {
            dtoList.add(this.po2dto(attach));
        }
        return dtoList;
    }

    public AttachRefDTO po2dto(Attach attach, AttachRef ref) {
        AttachRefDTO attachRef = new AttachRefDTO();
        attachRef.setFileId(attach.getFileId());
        attachRef.setClientFileName(attach.getClientFileName());
        attachRef.setContentLength(attach.getContentLength());
        attachRef.setContentType(attach.getContentType());
        attachRef.setCreateTime(
            DateTimeUtility.getFormatTime(attach.getCreateTime(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));
        attachRef.setDownLoadTimes(attach.getDownloadTimes());
        attachRef.setCreateUserId(attach.getCreateUserId());
        attachRef.setReadLevel(attach.getReadLevel());

        attachRef.setBelongId(ref.getBelongId());
        attachRef.setBelongType(ref.getBelongType());
        attachRef.setRefId(ref.getId());
        attachRef.setRemarks(ref.getRemarks());
        attachRef.setStatus(ref.getStatus());
        attachRef.setRefTime(ref.getCreateTime());
        attachRef.setRefUpdateTime(ref.getUpdateTime());
        return attachRef;
    }

    public List<AttachRefDTO> po2dtoList(Map<Long, Attach> attachMap, List<AttachRef> refs) {
        List<AttachRefDTO> attachRefs = new ArrayList<>();
        for (AttachRef attachRef : refs) {
            attachRefs.add(this.po2dto(attachMap.get(attachRef.getId()), attachRef));
        }
        return attachRefs;
    }

    public AttachDTO po2dto(Attach attach) {
        AttachDTO dto = new AttachDTO();
        if (attach == null) {
            return dto;
        }
        dto.setClientFileName(attach.getClientFileName());
        dto.setCreateUserId(attach.getCreateUserId());
        dto.setCreateTime(
            DateTimeUtility.getFormatTime(attach.getCreateTime(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS));
        dto.setDownLoadTimes(attach.getDownloadTimes());
        dto.setFileId(attach.getFileId());
        dto.setContentLength(attach.getContentLength());
        dto.setContentType(attach.getContentType());
        dto.setReadLevel(attach.getReadLevel());

        return dto;
    }

    public AttachRef attachRemark2Ref(Long belongId, String belongType, AttachRemark attachRemark) {
        long current = System.currentTimeMillis();
        AttachRef attachRef = new AttachRef();
        attachRef.setBelongId(belongId);
        attachRef.setBelongType(belongType);
        attachRef.setCreateTime(current);
        attachRef.setUpdateTime(current);
        attachRef.setRemarks(attachRemark.getRemark());
        attachRef.setFileId(attachRemark.getFileId());
        attachRef.setId(attachRemark.getRefId());
        attachRef.setStatus(StatusRecord.ENABLE.ordinal());
        return attachRef;
    }
}
