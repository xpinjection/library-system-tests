package com.xpinjection.library;

import com.xpinjection.library.config.EnvironmentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(EnvironmentConfig.class)
public class LibrarySystemTestsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibrarySystemTestsApplication.class, args);
    }

}
