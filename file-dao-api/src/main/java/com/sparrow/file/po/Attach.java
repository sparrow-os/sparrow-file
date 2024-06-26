package com.sparrow.file.po;

import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.dao.PO;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "attach")
public class Attach extends PO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 文件id(文件的唯一标识)
     */
    private String serialNumber;
    private String contentType;
    private String pathKey;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Attach() {
    }

    /**
     * FOR download file
     *
     * @param clientFileName
     * @param contentType
     */
    public Attach(String clientFileName, String contentType, Long authorId) {
        this.downloadTimes = 0L;
        this.setCreateUserId(authorId);
        this.setGmtCreate(System.currentTimeMillis());
        this.clientFileName = clientFileName;
        this.contentType = contentType;
        this.contentLength = 0L;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11)")
    @MethodOrder(order = 0)
    public Long getId() {
        return this.id;
    }

    @Column(name = "serial_number", columnDefinition = "varchar(64)", unique = true)
    @MethodOrder(order = 1)
    public String getSerialNumber() {
        return serialNumber;
    }

    @Column(name = "client_file_name", columnDefinition = "varchar(256) DEFAULT ''", updatable = false, nullable = false)
    @MethodOrder(order = 2)
    public String getClientFileName() {
        return clientFileName;
    }

    @Column(name = "download_times", columnDefinition = "int(11) UNSIGNED DEFAULT 0", nullable = false, updatable = false)
    @MethodOrder(order = 3)
    public Long getDownloadTimes() {
        return downloadTimes;
    }

    @Column(name = "content_length", columnDefinition = "int(11) UNSIGNED DEFAULT 0", nullable = false)
    @MethodOrder(order = 4)
    public Long getContentLength() {
        return contentLength;
    }

    @Column(name = "content_type", columnDefinition = "varchar(256) DEFAULT ''", updatable = false, nullable = false)
    @MethodOrder(order = 5)
    public String getContentType() {
        return this.contentType;
    }

    @Column(name = "path_key", columnDefinition = "varchar(32) DEFAULT ''", updatable = false, nullable = false)
    @MethodOrder(order = 5.1F)
    public String getPathKey() {
        return pathKey;
    }


    public void setPathKey(String pathKey) {
        this.pathKey = pathKey;
    }

    @Column(name = "business_type", columnDefinition = "tinyint(10) DEFAULT 0", updatable = false, nullable = false)
    @MethodOrder(order = 5.3F)
    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    @Column(name = "business_id", columnDefinition = "tinyint(10) UNSIGNED DEFAULT 0", updatable = false, nullable = false)
    @MethodOrder(order = 5.5F)
    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }

    public void setDownloadTimes(Long downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
