package com.sparrow.file.controller;

import com.sparrow.file.api.AttachService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Controller;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    @Inject
    private AttachService attachService;

    @GetMapping("/delete-file/{fileId}")
    public void deleteAttach(String fileId) throws BusinessException {
        this.attachService.deleteAttach(fileId);
    }
}
