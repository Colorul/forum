package com.msoft.worker.repository.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<WechatUser, String> {
    Optional<WechatUser> findFirstByOpenIdOrderByCreatedDateDesc(String openId);
}
