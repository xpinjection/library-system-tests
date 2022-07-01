package com.xpinjection.library.data;

import com.xpinjection.library.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Alimenkou Mikalai
 */
public interface BookDao extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
}
