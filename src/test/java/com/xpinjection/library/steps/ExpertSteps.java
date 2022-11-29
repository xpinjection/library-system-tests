package com.xpinjection.library.steps;

import com.xpinjection.library.client.dto.BookDto;
import com.xpinjection.library.config.EnvironmentConfig;
import com.xpinjection.library.restassured.RestAssuredTracingFilter;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpertSteps {
    private final Tracer tracer;
    private final EnvironmentConfig env;

    @Step("Create expert with recommendations")
    @NewSpan("add-expert")
    public int createExpert(String recommendations) {
        RestAssured.baseURI = env.getServices().get("library").getUrl();
        LOG.info("Create an expert with recommendations: {}", recommendations);
        return given()
                //.filter(new OpenApiValidationFilter(RestAssured.baseURI + "/v1/library-api.yaml"))
                .filter(new AllureRestAssured())
                .filter(new RestAssuredTracingFilter(tracer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("""
                            {
                              "name": "Mikalai",
                              "contact": "+38099023546",
                              "recommendations": [%s]
                            }""".formatted(recommendations))
            .when()
                .post("/experts")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().jsonPath().get("id");
    }

    @Step("Build recommendations from all found books")
    public String buildRecommendations(List<BookDto> books) {
        return books.stream()
                .map(book -> "\"" + book.getName() + " by " + book.getAuthor() + "\"")
                .collect(Collectors.joining(","));
    }
}
