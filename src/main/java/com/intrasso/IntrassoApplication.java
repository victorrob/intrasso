package com.intrasso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IntrassoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntrassoApplication.class, args);
    }

}
