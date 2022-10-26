package com.sparrow.file;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.container.impl.SparrowContainer;
import com.sparrow.file.api.AttachService;
import com.sparrow.orm.query.sql.CriteriaProcessor;
import org.junit.After;
import org.junit.Before;

/**
 * @author harry
 */
public class FileSuper {
    protected AttachService attachService;
    protected CriteriaProcessor criteriaProcessor;

    @Before
    public void setup() {
        Container container = new SparrowContainer();
        container.init(new ContainerBuilder());
        attachService = container.getBean("attachService");
        criteriaProcessor = container.getBean("criteriaProcessor");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("end");
    }
}
