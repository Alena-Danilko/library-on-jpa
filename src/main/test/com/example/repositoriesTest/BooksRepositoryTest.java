package com.example.repositoriesTest;

import com.example.models.Book;
import com.example.repositories.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BooksRepositoryTest {
    @Mock
    private BooksRepository booksRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByTitleStartingWithSuccessfully(){
        Book book1 = new Book("The Master and Margarita", "Bulgakov Mikhail", 1967);
        Book book2 = new Book("The Master", "Bulgakov Mikhail", 1969 );
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        when(booksRepository.findByTitleStartingWith("The")).thenReturn(bookList);

        List<Book> foundBookList = booksRepository.findByTitleStartingWith("The");
        assertEquals(2, foundBookList.size());
        assertTrue(foundBookList.contains(book1));
        assertTrue(foundBookList.contains(book2));
    }

    @Test
    public void testFindByTitleStartingWithUnsuccessfully(){
        when(booksRepository.findByTitleStartingWith("The Master?????")).thenReturn(new ArrayList<>());

        List<Book> foundBookList = booksRepository.findByTitleStartingWith("The Master?????");
        assertTrue(foundBookList.isEmpty());
    }
}
