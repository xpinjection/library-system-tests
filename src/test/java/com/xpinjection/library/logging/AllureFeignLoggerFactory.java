package com.xpinjection.library.logging;

import feign.Logger;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AllureFeignLoggerFactory implements FeignLoggerFactory {
    @Override
    public Logger create(Class<?> type) {
        return new AllureFeignLogger(type);
    }
}
