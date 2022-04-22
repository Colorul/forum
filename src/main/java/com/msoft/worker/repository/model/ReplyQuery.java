package com.msoft.worker.repository.model;

import com.msoft.worker.audit.AbstractAuditingEntity;
import com.msoft.worker.repository.domain.worker.Reply;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Setter
@Getter
@ApiModel(value = "话题查询器")
public class ReplyQuery {
    private String topicId;
    private static final String parentId = "0";
    private List<String> idList;

    private Query buildQuery() {
        Query query = new Query();
        if (StringUtils.hasText(this.getTopicId())) {
            Pattern pattern = Pattern.compile("^.*" + this.getTopicId() + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(Reply.Fields.topicId).regex(pattern));
        }


        if (!CollectionUtils.isEmpty(this.getIdList())) {
            query.addCriteria(Criteria.where(AbstractAuditingEntity.Fields.id).in(getIdList()));
        }

        Pattern pattern = Pattern.compile("^.*" + parentId + ".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where(Reply.Fields.parentId).regex(pattern));

        query.with(Sort.by(Sort.Direction.DESC, AbstractAuditingEntity.Fields.createdDate));
        return query;
    }

    public Page<Reply> buildPage(MongoTemplate mongoTemplate, Pageable pageable) {
        Query query = this.buildQuery();
        long total = mongoTemplate.count(query, Reply.class);
        return new PageImpl<>(mongoTemplate.find(query.with(pageable), Reply.class), pageable, total);
    }
}
