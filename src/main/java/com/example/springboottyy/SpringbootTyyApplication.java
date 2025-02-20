package com.example.springboottyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


//@EnableCaching //开启缓存

@SpringBootApplication
@EnableJpaAuditing
public class SpringbootTyyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTyyApplication.class, args);
    }

}
