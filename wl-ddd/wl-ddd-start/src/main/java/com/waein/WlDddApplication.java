package com.waein;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.waein")
@MapperScan("com.waein.infrastructure.**.mapper")
public class WlDddApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlDddApplication.class, args);
    }
}
