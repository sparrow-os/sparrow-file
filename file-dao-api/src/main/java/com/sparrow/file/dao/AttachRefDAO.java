package com.sparrow.file.dao;

import com.sparrow.file.po.AttachRef;
import com.sparrow.file.query.EnableAttachQueryDTO;
import com.sparrow.protocol.dao.DaoSupport;
import java.util.List;

public interface AttachRefDAO extends DaoSupport<AttachRef, Long> {

    void enable(EnableAttachQueryDTO enableAttach) throws Exception;

    void disable(String business, Long id) throws Exception;

    List<AttachRef> getAttachList(Long belongId, String belongType) throws Exception;

    List<AttachRef> getEnableAttachList(Long belongId, String belongType) throws Exception;

}
