package com.msoft.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class SchoolService {
    @Value("classpath:school.json")
    private Resource schoolResource;

    @Cacheable(cacheNames = "SCHOOL")
    public List<School> search() {
        return this.readFromResource(schoolResource);
    }


    private <T> List<T> readFromResource(Resource resource) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String json = br.lines().collect(Collectors.joining());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, School.class));
        } catch (IOException e) {
            log.error("系统错误: 读取JSON数据错误," + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Setter
    @Getter
    public static class School {
        private String province, city, name;
    }
}
