package com.sparrow.file.dao.impl;

import com.sparrow.file.dao.AttachRefDAO;
import com.sparrow.file.po.AttachRef;
import com.sparrow.file.query.AttachRemark;
import com.sparrow.file.query.EnableAttachQueryDTO;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;

import com.sparrow.protocol.enums.StatusRecord;
import java.util.List;
import javax.inject.Named;

@Named("attachRefDao")
public class AttachRefDAOImpl extends ORMStrategy<AttachRef, Long> implements AttachRefDAO {
    @Override
    public void enable(EnableAttachQueryDTO enableAttach) {
        for (AttachRemark attachRemark : enableAttach.getAttachRemarkList()) {
            UpdateCriteria updateCriteria = new UpdateCriteria();
            updateCriteria.
                set(UpdateSetClausePair.field("attachRef.status").equal(StatusRecord.ENABLE.ordinal())).
                set(UpdateSetClausePair.field("attachRef.belongId").equal(enableAttach.getBelongId())).
                set(UpdateSetClausePair.field("attachRef.belongType").equal(enableAttach.getBelongBusiness())).
                set(UpdateSetClausePair.field("attachRef.updateTime").equal(System.currentTimeMillis()));

            if (attachRemark.getRemark() != null) {
                updateCriteria.set(UpdateSetClausePair.field("attachRef.remarks").equal(attachRemark.getRemark()));
            }

            updateCriteria.setWhere(Criteria.field("attachRef.id").equal(attachRemark.getRefId()));
            this.update(updateCriteria);
        }
    }

    @Override
    public void disable(String business, Long belongId) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field("attachRef.status").equal(StatusRecord.DISABLE.ordinal()))
            .set(UpdateSetClausePair.field("attachRef.updateTime").equal(System.currentTimeMillis()));

        updateCriteria.setWhere(BooleanCriteria.criteria(Criteria.field("attachRef.belongId").equal(belongId)).
            and(Criteria.field("attachRef.belongType").equal(business)));
        this.update(updateCriteria);
    }

    @Override
    public List<AttachRef> getAttachList(Long belongId, String belongBusiness) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(BooleanCriteria.criteria(
            Criteria.field("attachRef.belongType").equal(belongBusiness))
            .and(Criteria.field("attachRef.belongId").equal(belongId))
        );
        searchCriteria.addOrderCriteria(OrderCriteria.asc("attachRef.createTime"));
        return this.getList(searchCriteria);
    }

    @Override
    public List<AttachRef> getEnableAttachList(Long belongId, String belongBusiness) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(BooleanCriteria.criteria(
            Criteria.field("attachRef.belongType").equal(belongBusiness))
            .and(Criteria.field("attachRef.belongId").equal(belongId))
            .and(Criteria.field("attachRef.status").equal(StatusRecord.ENABLE.ordinal()))
        );
        searchCriteria.addOrderCriteria(OrderCriteria.asc("attachRef.createTime"));
        return this.getList(searchCriteria);
    }
}
