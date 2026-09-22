/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.sparrow.file.assemble;

import com.sparrow.context.SessionContext;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.po.Attach;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.StatusRecord;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Named
public class AttachAssemble {

    public Attach assembleNewAttach(AttachUploadParam bo) {
        Attach po = new Attach();
        LoginUser loginUser = SessionContext.getLoginUser();
        po.setSerialNumber(bo.getSerialNumber());
        po.setClientFileName(bo.getClientFileName());
        po.setCreateUserId(bo.getCreateUserId());
        po.setGmtCreate(System.currentTimeMillis());
        po.setDownloadTimes(0L);
        po.setContentLength((long) bo.getContentLength());
        po.setContentType(bo.getContentType());
        po.setCreateUserName(loginUser.getUserName());
        po.setModifiedUserName(loginUser.getUserName());
        po.setBusinessType(bo.getPathKey());
        po.setPathKey(bo.getPathKey());
        po.setGmtModified(System.currentTimeMillis());
        po.setBusinessId(0);
        po.setStatus(StatusRecord.ENABLE);
        po.setDeleted(false);
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
