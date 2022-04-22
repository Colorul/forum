package com.msoft.worker.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.problem.ProblemModule;


@Configuration
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class WebConfiguration {

    @Value("${spring.data.mongodb.database}")
    String db;

    /**
     * 配置ProblemModule, 禁止其返回堆栈异常
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
        return (Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder)  ->
                jacksonObjectMapperBuilder.modules(new ProblemModule().withStackTraces(false));
    }


    @Bean
    public GridFSBucket getGridFSBuckets(MongoClient mongoClient) {
        return GridFSBuckets.create(mongoClient.getDatabase(db));
    }
}
