package com.msoft.worker.repository.domain.worker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MsgRepository extends MongoRepository<Msg, String> {
    Page<Msg> findByUserIdOrderByStatusAscCreatedDateDesc(String userId, Pageable pageable);
}
