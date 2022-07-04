package com.xpinjection.library.logging;

import feign.Request;
import feign.Response;
import feign.Util;
import feign.slf4j.Slf4jLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import io.qameta.allure.attachment.http.HttpResponseAttachment;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;
import static io.qameta.allure.attachment.http.HttpRequestAttachment.Builder.create;
import static java.util.stream.Collectors.toMap;

public class AllureFeignLogger extends Slf4jLogger {
    public AllureFeignLogger(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        super.logRequest(configKey, logLevel, request);

        var requestAttachmentBuilder = create("Request", request.url())
                .setMethod(request.httpMethod().name())
                .setHeaders(joinHeaders(request.headers()));

        if (request.body() != null) {
            if (request.charset() != null) {
                requestAttachmentBuilder.setBody(new String(request.body(), request.charset()));
            } else {
                requestAttachmentBuilder.setBody("Binary data");
            }
        }

        addAttachment(requestAttachmentBuilder.build(), "http-request.ftl");
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        var buffered = super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);

        var protocolVersion = resolveProtocolVersion(buffered.protocolVersion());
        var statusLine = protocolVersion + " " + buffered.status() +
                (buffered.reason() != null ? " " + buffered.reason() : "");
        var responseAttachmentBuilder = HttpResponseAttachment.Builder.create(statusLine)
                .setResponseCode(buffered.status())
                .setHeaders(joinHeaders(buffered.headers()));

        byte[] bodyData = Util.toByteArray(buffered.body().asInputStream());
        if (bodyData.length > 0) {
            responseAttachmentBuilder.setBody(decodeOrDefault(bodyData, UTF_8, "Binary data"));
        }

        addAttachment(responseAttachmentBuilder.build(), "http-response.ftl");

        if (buffered == response && bodyData.length > 0) {
            return response.toBuilder().body(bodyData).build();
        }
        return buffered;
    }

    private void addAttachment(AttachmentData attachment, String templateName) {
        new DefaultAttachmentProcessor().addAttachment(attachment,
                new FreemarkerAttachmentRenderer(templateName)
        );
    }

    private Map<String, String> joinHeaders(Map<String, Collection<String>> headers) {
        return headers.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
    }
}
