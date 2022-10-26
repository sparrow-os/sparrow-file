package com.sparrow.file.controller;

import com.sparrow.file.api.AttachService;
import com.sparrow.mvc.RequestParameters;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Controller;
import javax.inject.Inject;

@Controller
public class FileController {
    @Inject
    private AttachService attachService;

    @RequestParameters("fileId")
    public void deleteAttach(String fileId) throws BusinessException {
        this.attachService.deleteAttach(fileId);
    }
}
