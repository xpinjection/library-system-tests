package com.xpinjection.library;

import com.xpinjection.library.client.dto.BookDto;
import com.xpinjection.library.data.BookRegistry;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Library")
@Feature("Expert recommendations")
class ExpertSystemTest extends AbstractSystemTest {
    @Autowired
    private BookRegistry bookRegistry;

    @BeforeEach
    void init() {
        RestAssured.baseURI = env.getServices().get("library").getUrl();
    }

    @Test
    @Tag("library")
    @TmsLink("LB-T159")
    @Issue("US-1234")
    @Story("Expert could recommend any number of books in free format")
    void booksCouldBeRecommendedByTheExpert() {
        startTrace("LB-T159");
        var books = findBooksInRegistry("Josh Bloch");

        var recommendations = buildRecommendations(books);
        int id = createExpert(recommendations);

        assertThat(id).isPositive();
    }

    @Step("Create expert with recommendations")
    private int createExpert(String recommendations) {
        var span = tracer.startScopedSpan("add-expert");
        try {
            return given()
                    //.filter(new OpenApiValidationFilter(RestAssured.baseURI + "/v1/library-api.yaml"))
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("X-B3-TraceId", span.context().traceIdString())
                    .header("X-B3-SpanId", span.context().spanIdString())
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
        } catch (RuntimeException e) {
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    @Step("Build recommendations from all found books")
    private String buildRecommendations(List<BookDto> books) {
        return books.stream()
                .map(book -> "\"" + book.getName() + " by " + book.getAuthor() + "\"")
                .collect(Collectors.joining(","));
    }

    @Step("Find books in registry by author")
    private List<BookDto> findBooksInRegistry(String author) {
        return bookRegistry.getBooksByAuthor(author);
    }
}
