package com.sparrow.file.dto;

import lombok.Data;

@Data
public class AttachDTO {
    private Long id;
    private String contentType;
    private Integer width;
    private Integer height;
    private Integer businessType;
    private Integer businessId;
    /**
     * 文件的实际大小
     */
    private Long contentLength;
    /**
     * 下载次数
     */
    private Long downloadTimes;
    /**
     * 客户端文件名
     */
    private String clientFileName;
    private String gmtCreate;
    private Long createUserId;
    private String url;
}

