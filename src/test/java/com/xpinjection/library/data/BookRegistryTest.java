package com.xpinjection.library.data;

import com.xpinjection.library.client.LibraryClient;
import com.xpinjection.library.client.dto.BookDto;
import com.xpinjection.library.data.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRegistryTest {
    private final BookDto book = new BookDto(9L, "Name", "a");
    private BookRegistry registry;

    @Mock
    private BookDao bookDao;

    @Mock
    private LibraryClient libraryClient;

    @BeforeEach
    void init() {
        registry = new BookRegistry(bookDao, libraryClient);
    }

    @Test
    void ifBooksAreFoundInTestDbThenTheyAreReused() {
        when(bookDao.findByAuthor("a")).thenReturn(List.of(new Book(9L, "Name", "a")));

        assertThat(registry.getBooksByAuthor("a")).containsExactly(book);
    }

    @Test
    void ifBooksAreNotFoundInTestDbThenTheyAreRetrievedFromLibrary() {
        when(bookDao.findByAuthor("a")).thenReturn(emptyList());
        when(libraryClient.findByAuthor("a")).thenReturn(List.of(book));


        assertThat(registry.getBooksByAuthor("a")).containsExactly(book);
    }
}