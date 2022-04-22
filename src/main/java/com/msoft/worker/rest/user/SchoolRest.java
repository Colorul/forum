package com.msoft.worker.rest.user;

import com.msoft.worker.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("school")
@RestController
@Api(value = "学校列表API", tags = "学校列表API")
public record SchoolRest(SchoolService schoolService) {

    @GetMapping(produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "分页查询消息")
    public Mono<List<SchoolService.School>> page(@RequestParam(required = false, name = "name") String name) {
        List<SchoolService.School> schools = this.schoolService.search();
        if (StringUtils.hasText(name)) {
            schools = schools.stream().filter(item -> item.getName().contains(name)).collect(Collectors.toList());
        }
        return Mono.just(schools.stream().sorted((v1,v2) -> Collator.getInstance(Locale.CHINA).compare(v1.getProvince() + v1.getCity(),v2.getProvince()+v2.getCity())).toList());
    }

}
