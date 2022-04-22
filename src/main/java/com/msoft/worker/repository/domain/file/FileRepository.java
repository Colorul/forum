package com.msoft.worker.repository.domain.file;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<FileDocument, String> {
    Optional<FileDocument> findByMd5(String md5);
    Optional<FileDocument> findByGridfsId(String gridfsId);
}
