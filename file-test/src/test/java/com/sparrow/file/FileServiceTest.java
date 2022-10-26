package com.sparrow.file;

import com.sparrow.enums.Business;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.dto.AttachRefDTO;
import com.sparrow.file.query.AttachRemark;
import com.sparrow.protocol.BusinessException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by harry on 17/11/18.
 */
public class FileServiceTest extends FileSuper {

    @Test
    public void multiEnableFileTest() throws Exception {
        List<AttachRemark> attachRemarkList = new ArrayList<AttachRemark>();
        //attachRemarkList.add(new AttachRemark(2L, "图片说明6405"));
        attachRemarkList.add(new AttachRemark(1L, "2", "333"));
        this.attachService.modifyStatus(1l, Business.CMS.name(), attachRemarkList);
    }

    @Test
    public void attachListTest() throws BusinessException {
        List<AttachRefDTO> attachDTOList = this.attachService.getEnableAttachList(1L, Business.CMS.name());
        Assert.assertTrue(attachDTOList.size() > 0);
    }

    @Test
    public void getAttachTest() throws BusinessException {
        AttachDTO attachDTO = this.attachService.getAttach("2");
        Assert.assertTrue(attachDTO != null);
    }
}
