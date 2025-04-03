package org.dci.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private int bookId;
    private String title;
    private Author author;
    private int publicationYear;
    private boolean isAvailable;


}
