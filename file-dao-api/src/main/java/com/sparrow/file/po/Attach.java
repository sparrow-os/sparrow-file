package com.sparrow.file.po;

import com.sparrow.protocol.dao.PO;
import lombok.Data;

import javax.persistence.*;

@Table(name = "attach")
@Data
public class Attach extends PO {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11)")
    private Long id;
    /**
     * 文件id(文件的唯一标识)
     */
    @Column(name = "serial_number", columnDefinition = "varchar(64)", unique = true)
    private String serialNumber;
    @Column(name = "content_type", columnDefinition = "varchar(256) DEFAULT ''", updatable = false, nullable = false)
    private String contentType;
    @Column(name = "path_key", columnDefinition = "varchar(32) DEFAULT ''", updatable = false, nullable = false)
    private String pathKey;
    @Column(name = "business_type", columnDefinition = "tinyint(10) DEFAULT 0", updatable = false, nullable = false)
    private Integer businessType;
    @Column(name = "business_id", columnDefinition = "tinyint(10) UNSIGNED DEFAULT 0", updatable = false, nullable = false)
    private Integer businessId;
    /**
     * 文件的实际大小
     */
    @Column(name = "content_length", columnDefinition = "int(11) UNSIGNED DEFAULT 0", nullable = false)
    private Long contentLength;
    /**
     * 下载次数
     */
    @Column(name = "download_times", columnDefinition = "int(11) UNSIGNED DEFAULT 0", nullable = false, updatable = false)
    private Long downloadTimes;
    /**
     * 客户端文件名
     */
    @Column(name = "client_file_name", columnDefinition = "varchar(256) DEFAULT ''", updatable = false, nullable = false)
    private String clientFileName;

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

}
