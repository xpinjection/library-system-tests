package com.xpinjection.library.steps;

import com.xpinjection.library.client.dto.BookDto;
import com.xpinjection.library.data.BookRegistry;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookSteps {
    private final BookRegistry bookRegistry;

    @Step("Find books in registry by author")
    public List<BookDto> findBooksInRegistry(String author) {
        return bookRegistry.getBooksByAuthor(author);
    }
}
