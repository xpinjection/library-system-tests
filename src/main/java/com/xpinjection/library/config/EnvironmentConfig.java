package com.xpinjection.library.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "env")
public class EnvironmentConfig {
    /**
     * Named set of available services
     */
    private Map<String, ServiceConfig> services = new HashMap<>();

    @Valid
    @Data
    public static class ServiceConfig {
        /**
         * Url where service API is exposed
         */
        @NotBlank
        private String url;
    }
}
