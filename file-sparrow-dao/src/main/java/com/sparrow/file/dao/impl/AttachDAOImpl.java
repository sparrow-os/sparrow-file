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
package com.sparrow.file.dao.impl;

import com.sparrow.file.dao.AttachDAO;
import com.sparrow.file.po.Attach;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import jakarta.inject.Named;
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
