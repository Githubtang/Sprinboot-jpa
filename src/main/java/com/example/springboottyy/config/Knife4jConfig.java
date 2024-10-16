package com.example.springboottyy.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Insight
 * @Description: API文档配置类
 * @Date: 2024/10/12 23:59
 * @Version: 1.0
 */
@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi apiGroup(){
        return GroupedOpenApi.builder()
                .group("default")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("API 文档")
                        .description("权限管理系统API文档")
                        .version("1.0")
                        .contact(new Contact())
                ))
                .pathsToMatch("/**")
                .build();
    }
}
