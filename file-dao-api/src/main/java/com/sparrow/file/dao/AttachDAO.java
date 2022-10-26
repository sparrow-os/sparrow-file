package com.sparrow.file.dao;

import com.sparrow.file.po.Attach;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;
import java.util.Map;

public interface AttachDAO extends DaoSupport<Attach, Long> {
    void addDownLoadTimes(String fileId) throws Exception;

    Map<Long, Attach> getAttachMap(List<Long> fileId);
}
