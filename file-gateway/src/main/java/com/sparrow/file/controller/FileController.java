package com.sparrow.file.controller;

import com.sparrow.file.api.AttachService;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.protocol.BusinessException;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    @Inject
    private AttachService attachService;

    @GetMapping("/delete-file/{fileId}")
    public void deleteAttach(Long fileId) throws BusinessException {
        this.attachService.deleteImageById(fileId);
    }
}
