package com.xpinjection.library.data.model;

import com.xpinjection.library.client.dto.BookDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
    @Id
    private Long id;
    private String name;
    private String author;

    public BookDto toDto() {
        return new BookDto(id, name, author);
    }

    public static Book fromDto(BookDto dto) {
        return new Book(dto.getId(), dto.getName(), dto.getAuthor());
    }
}
