package com.xpinjection.library;

import brave.ScopedSpan;
import brave.Tracer;
import com.xpinjection.library.config.EnvironmentConfig;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public abstract class AbstractSystemTest {
    @Autowired
    protected EnvironmentConfig env;

    @Autowired
    protected Tracer tracer;

    protected ScopedSpan trace;

    @AfterEach
    void cleanup() {
        if (trace != null) {
            trace.finish();
        }
    }

    protected void startTrace(String name) {
        trace = tracer.startScopedSpan(name);
        var traceId = trace.context().traceIdString();
        Allure.link("Trace in Jaeger", env.getJaegerTraceUrlTemplate().replace("{traceId}", traceId));
        Allure.link("Trace in Kibana", env.getKibanaTraceUrlTemplate().replace("{traceId}", traceId));
    }
}
