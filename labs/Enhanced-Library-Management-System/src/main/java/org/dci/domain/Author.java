package org.dci.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Author {
    private int authorId;
    private String authorName;
    private int booksSold;
    private boolean isBestSeller;
}
