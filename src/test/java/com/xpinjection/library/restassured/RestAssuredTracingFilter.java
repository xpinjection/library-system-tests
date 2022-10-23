package com.xpinjection.library.restassured;

import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Tracer;

@RequiredArgsConstructor
public class RestAssuredTracingFilter implements OrderedFilter {
    private final Tracer tracer;

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        var currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            requestSpec.header("X-B3-TraceId", currentSpan.context().traceId())
                    .header("X-B3-SpanId", currentSpan.context().spanId());
        }
        return ctx.next(requestSpec, responseSpec);
    }
}
