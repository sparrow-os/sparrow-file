package com.sparrow.file;

import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachRemark;
import com.sparrow.file.param.EnableAttachParam;
import com.sparrow.protocol.BusinessException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 17/11/18.
 */
public class FileServiceTest extends FileSuper {

    @Test
    public void getAttachTest() throws BusinessException {
        AttachDTO attachDTO = this.attachService.getAttach(2L);
        Assert.assertTrue(attachDTO != null);
    }
}
