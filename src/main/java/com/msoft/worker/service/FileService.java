package com.msoft.worker.service;

import cn.hutool.crypto.SecureUtil;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.msoft.worker.config.security.jwt.JwtTokenProvider;
import com.msoft.worker.exception.ExceptionWrapper;
import com.msoft.worker.repository.domain.file.FileDocument;
import com.msoft.worker.repository.domain.file.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public record FileService(FileRepository fileRepository,
                          GridFsTemplate gridFsTemplate,
                          JwtTokenProvider tokenProvider,
                          GridFSBucket gridFSBucket) {

    public void upload(@Valid @NotNull File file, String uuid) {
        try {
            byte[] bytes = new FileInputStream(file).readAllBytes();
            upload(bytes, file.getName(), file.getName(),
                    file.getName(), (long) bytes.length, uuid);

        } catch (Exception e) {
            log.error("上传文件失败,请联系管理员处理！", e);
            throw new ExceptionWrapper("上传文件失败,请联系管理员处理！");
        }
    }

    private void upload(byte[] bytes, String contentType, String name, String originalFilename, Long size, String uuid) {
        String md5 = SecureUtil.md5(new ByteArrayInputStream(bytes));
        FileDocument document = this.fileRepository.findByMd5(md5).orElse(null);
        if (document != null) {
            return;
        }
        document = new FileDocument();
        if (!StringUtils.hasText(uuid)) {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        this.gridFsTemplate.store(new ByteArrayInputStream(bytes), uuid, contentType);
        document.setContentType(contentType);
        document.setGridfsId(uuid);
        document.setMd5(md5);
        document.setName(name);
        document.setOriginalFilename(originalFilename);
        document.setSize(size);
        this.fileRepository.save(document);
    }

    /**
     * 下载文档
     *
     * @param id 文件id
     */
    public ResponseEntity<Object> download(@NotBlank String id) throws UnsupportedEncodingException {
        Optional<FileDocument> file = this.fileRepository.findById(id);
        if (file.isPresent()) {
            this.buildContent(file.get());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + new String(file.get().getOriginalFilename().getBytes("GBK"), StandardCharsets.ISO_8859_1))
                    .header(HttpHeaders.CONTENT_TYPE, file.get().getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .body(file.get().getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }
    }

    private void buildContent(FileDocument fileDocument) {
        Query gridQuery = new Query().addCriteria(Criteria.where("filename").is(fileDocument.getGridfsId()));
        try {
            GridFSFile fsFile = gridFsTemplate.findOne(gridQuery);
            if (fsFile == null) {
                throw new ExceptionWrapper("File not found");
            }
            GridFSDownloadStream in = gridFSBucket.openDownloadStream(fsFile.getObjectId());
            if (in.getGridFSFile().getLength() > 0) {
                GridFsResource resource = new GridFsResource(fsFile, in);
                InputStream inputStream = resource.getInputStream();
                fileDocument.setContent(StreamUtils.copyToByteArray(inputStream));
            }
        } catch (IOException ex) {
            throw new ExceptionWrapper("File not found");
        }
    }
}
