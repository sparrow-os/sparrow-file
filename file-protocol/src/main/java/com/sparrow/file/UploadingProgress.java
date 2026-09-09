package com.sparrow.file;

import com.sparrow.protocol.POJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadingProgress implements POJO {
    private Long contentLength;
    private Long readLength;
    private String fileUrl;
    private String clientFileName;
    private Long fileId;
    private String contentType;
    private String status;
    private String error;

    private String humanReadableContentLength;
    private String humanReadableReadLength;
}
