package com.xpinjection.library;

import com.xpinjection.library.config.EnvironmentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public abstract class AbstractSystemTest {
    @Autowired
    protected EnvironmentConfig env;
}
