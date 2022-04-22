package com.msoft.worker.repository.domain.worker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {
    Page<Topic> findByUserIdOrderByCreatedDateDesc(String userId, Pageable pageable);
}
