package com.xpinjection.library;

import com.xpinjection.library.steps.BookSteps;
import com.xpinjection.library.steps.ExpertSteps;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Library")
@Feature("Expert recommendations")
class ExpertSystemTest extends AbstractSystemTest {
    @Autowired
    private BookSteps bookSteps;
    @Autowired
    private ExpertSteps expertSteps;

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
        var books = bookSteps.findBooksInRegistry("Josh Bloch");

        var recommendations = expertSteps.buildRecommendations(books);
        int id = expertSteps.createExpert(recommendations);

        assertThat(id).isPositive();
    }
}
