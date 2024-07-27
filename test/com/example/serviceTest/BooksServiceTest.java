package com.example.serviceTest;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.BooksRepository;
import com.example.service.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BooksServiceTest {
    @Mock
    private BooksRepository booksRepository;
    private BooksService booksService;

    private Book book1 = new Book("The Master and Margarita", "Bulgakov Mikhail", 1967);
    private Book book2 = new Book("The Master", "Bulgakov Mikhail", 1969);
    private Person owner = new Person("Test Person", 1998);
    private List<Book> expectedBooks = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        booksService = new BooksService(booksRepository);
        expectedBooks.add(book1);
        expectedBooks.add(book2);
    }

    @Test
    public void testFindAllWithoutSort(){
        when(booksRepository.findAll()).thenReturn(expectedBooks);

        List<Book> foundBooks = booksService.findAll(false);
        assertFalse(foundBooks.isEmpty());
        assertEquals(2, foundBooks.size());
        assertEquals(expectedBooks, foundBooks);
    }

    @Test
    public void testFindAllWithSort(){
        when(booksRepository.findAll(Sort.by("yearOfPublication"))).thenReturn(expectedBooks);

        List<Book> foundBooks = booksService.findAll(true);
        assertEquals(expectedBooks, foundBooks);
    }

    @Test
    public void testFindWithPageWithoutSort(){
        Page<Book> booksPage = new PageImpl<>(expectedBooks);

        when(booksRepository.findAll(PageRequest.of(0, 2))).thenReturn(booksPage);

        List<Book> foundBooks = booksService.findWithPage(0, 2, false);
        assertEquals(expectedBooks, foundBooks);
        assertEquals(2, foundBooks.size());
    }

    @Test
    public void testFindWithPageWithSort(){
        Page<Book> booksPage = new PageImpl<>(expectedBooks);

        when(booksRepository.findAll(PageRequest.of(0, 2, Sort.by("yearOfPublication")))).thenReturn(booksPage);

        List<Book> foundBooks = booksService.findWithPage(0, 2, true);
        assertEquals(expectedBooks, foundBooks);
        assertEquals(1967, foundBooks.get(0).getYearOfPublication());
        assertEquals(1969, foundBooks.get(1).getYearOfPublication());
    }

    @Test
    public void testFindOne(){
        int id = 2;
        when(booksRepository.findById(id)).thenReturn(Optional.of(book1));

        Book foundBook = booksService.findOne(id);
        assertNotNull(foundBook);
        assertEquals(book1, foundBook);
    }

    @Test
    public void testSaveBook(){
        when(booksRepository.save(any(Book.class))).thenReturn(book1);
        booksService.save(book1);
        verify(booksRepository).save(book1);

        ArgumentCaptor<Book> result = ArgumentCaptor.forClass(Book.class);
        verify(booksRepository).save(result.capture());
        Book savedBook = result.getValue();

        assertEquals("The Master and Margarita", savedBook.getTitle());
        assertEquals("Bulgakov Mikhail", savedBook.getAuthor());
        assertEquals(1967, savedBook.getYearOfPublication());
    }

    @Test
    public void testUpdateBook(){
        int id = 1;
        book1.setId(1);

        when(booksRepository.findById(id)).thenReturn(Optional.of(book2));
        booksService.update(1, book2);

        verify(booksRepository).save(book2);
        assertEquals(id, book2.getId());
        verify(booksRepository).save(book2);
    }

    @Test
    public void testDeleteBook(){
        int id = 1;
        book1.setId(id);

        when(booksRepository.findById(id)).thenReturn(Optional.of(book1));
        booksService.delete(1);
        verify(booksRepository).deleteById(1);

        when(booksRepository.findById(id)).thenReturn(Optional.empty());
        assertFalse(booksRepository.findById(id).isPresent());
    }

    @Test
    public void testGetBookOwner(){
        int id = 2;
        book1.setOwner(owner);

        when(booksRepository.findById(id)).thenReturn(Optional.of(book1));

        Person person = booksService.getBookOwner(id);
        assertEquals(owner, person);
    }

    @Test
    public void testRelease(){
        int id = 2;
        book1.setOwner(owner);
        book1.setTakenIn(new Date());

        when(booksRepository.findById(id)).thenReturn(Optional.of(book1));
        booksService.release(id);

        assertNull(book1.getOwner());
        assertNull(book1.getTakenIn());
    }

    @Test
    public void testAssign(){
        int id = 2;
        book1.setOwner(null);
        book1.setTakenIn(null);

        when(booksRepository.findById(id)).thenReturn(Optional.of(book1));
        booksService.assign(id, owner);

        assertNotNull(book1.getOwner());
        assertNotNull(book1.getTakenIn());
    }

    @Test
    public void testSearchByTitle(){
        when(booksRepository.findByTitleStartingWith("The")).thenReturn(expectedBooks);

        List<Book> foundBook = booksService.searchByTitle("The");
        assertEquals(expectedBooks, foundBook);
    }
}

