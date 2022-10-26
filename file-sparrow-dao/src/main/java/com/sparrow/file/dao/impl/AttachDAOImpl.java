package com.sparrow.file.dao.impl;

import com.sparrow.file.dao.AttachDAO;
import com.sparrow.file.po.Attach;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Named("attachDao")
public class AttachDAOImpl extends ORMStrategy<Attach, Long> implements AttachDAO {
    private static Logger logger = LoggerFactory.getLogger(AttachDAOImpl.class);

    @Override
    public void addDownLoadTimes(String fileId) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field("attach.downloadTimes").add(1L));
        updateCriteria.setWhere(Criteria.field("attach.fileId").equal(fileId));
        this.update(updateCriteria);
    }

    @Override
    public Map<Long, Attach> getAttachMap(List<Long> fileId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field("attach.fileId").in(fileId));
        return this.getEntityMap(searchCriteria);
    }
}
