package com.msoft.worker.repository.model;

import com.msoft.worker.audit.AbstractAuditingEntity;
import com.msoft.worker.repository.domain.worker.Topic;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Setter
@Getter
@ApiModel(value = "话题查询器")
public class TopicQuery {
    private String keyword;
    private String school;
    private String userId;

    private Query buildQuery() {
        Query query = new Query();

        if (StringUtils.hasText(this.getKeyword())) {
            Pattern pattern = Pattern.compile("^.*" + this.getKeyword() + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(Topic.Fields.title).regex(pattern)
                    .orOperator(Criteria.where(Topic.Fields.content).regex(pattern)));
        }

        if (StringUtils.hasText(this.getSchool())) {
            Pattern pattern = Pattern.compile("^.*" + this.getSchool() + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(Topic.Fields.school).regex(pattern));
        }

        if (StringUtils.hasText(this.getUserId())) {
            Pattern pattern = Pattern.compile("^.*" + this.getUserId() + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(Topic.Fields.userId).regex(pattern));
        }
        query.with(Sort.by(Sort.Direction.DESC, AbstractAuditingEntity.Fields.createdDate));
        return query;
    }

    public Page<Topic> buildPage(MongoTemplate mongoTemplate, Pageable pageable) {
        Query query = this.buildQuery();
        long total = mongoTemplate.count(query, Topic.class);
        return new PageImpl<>(mongoTemplate.find(query.with(pageable), Topic.class), pageable, total);
    }
}
