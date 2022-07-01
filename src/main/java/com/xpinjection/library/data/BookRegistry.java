package com.xpinjection.library.data;

import com.xpinjection.library.client.LibraryClient;
import com.xpinjection.library.client.dto.BookDto;
import com.xpinjection.library.data.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookRegistry {
    private final BookDao bookDao;
    private final LibraryClient libraryClient;

    public List<BookDto> getBooksByAuthor(String author) {
        LOG.info("Find books in test DB by author: {}", author);
        var books = bookDao.findByAuthor(author);
        if (!books.isEmpty()) {
            LOG.info("Books found, reuse them");
            return books.stream()
                    .map(Book::toDto)
                    .toList();
        }
        LOG.info("Find books in Library by author: {}", author);
        var newBooks = libraryClient.findByAuthor(author);
        storeNewBooks(newBooks);
        return newBooks;
    }

    private void storeNewBooks(List<BookDto> books) {
        LOG.info("Store found books for reuse: {}", books);
        books.stream()
                .map(Book::fromDto)
                .forEach(bookDao::save);
    }
}
