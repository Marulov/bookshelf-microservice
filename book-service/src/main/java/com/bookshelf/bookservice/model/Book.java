package com.bookshelf.bookservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int bookYear;
    private String author;
    private String pressName;
    private String isbn;
}