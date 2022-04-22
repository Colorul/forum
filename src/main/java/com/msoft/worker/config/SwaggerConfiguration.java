package com.msoft.worker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    @Profile("dev")
    public Docket ProductApiDev() {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .enable(Boolean.TRUE)
                .pathMapping("/")
                .select()
                .build()
                .apiInfo(productApiInfo());
    }

    @Bean
    @Profile("prod")
    public Docket ProductApiProd() {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .enable(Boolean.FALSE)
                .pathMapping("/")
                .select()
                .build()
                .apiInfo(productApiInfo());
/*                return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .build()
                .apiInfo(productApiInfo());*/
    }

    private ApiInfo productApiInfo() {
        return new ApiInfo("文档名称",
                "文档用于开发阶段向其他API使用者提供参与说明",
                "1.0.0",
                "API TERMS URL",
                new Contact("limeng199207", "", "limeng199207@gmail.com"),
                "",
                "",
                new ArrayList<>());
    }
}
