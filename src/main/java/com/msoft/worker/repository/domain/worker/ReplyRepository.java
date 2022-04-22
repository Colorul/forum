package com.msoft.worker.repository.domain.worker;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface ReplyRepository extends MongoRepository<Reply, String> {
    List<Reply> findByTopicId(String topicId);
}
