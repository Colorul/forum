package com.msoft.worker.repository.domain.file;

import com.msoft.worker.audit.AbstractAuditingEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class FileDocument extends AbstractAuditingEntity {
    private String name;
    private String contentType;
    private String md5;
    private Long size;
    private String originalFilename;
    private String gridfsId;
    private String uri;
    @Transient
    private byte[] content;
}
