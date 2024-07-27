package com.example.controllersTest;

import com.example.controllers.BooksController;
import com.example.models.Book;
import com.example.models.Person;
import com.example.service.BooksService;
import com.example.service.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BooksControllerTest {
    @Mock
    private BooksService booksService;

    @Mock
    private PeopleService peopleService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private BooksController booksController;

    private int id;
    private Book book;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        id = 1;
        book = new Book("The Master and Margarita", "Bulgakov Mikhail", 1967);
    }

    @Test
    public void testIndex(){
        Integer page = null;
        Integer bookPerPage = null;
        boolean sortByYear = false;

        List<Book> books = new ArrayList<>();
        when(booksService.findAll(sortByYear)).thenReturn(books);
        String result = booksController.index(model, page, bookPerPage, sortByYear);
        assertEquals("books/index", result);
        verify(booksService).findAll(sortByYear);

        page = 2;
        bookPerPage = 2;
        sortByYear = true;

        when(booksService.findWithPage(page, bookPerPage, sortByYear)).thenReturn(books);
        result = booksController.index(model, page, bookPerPage, sortByYear);
        assertEquals("books/index", result);
        verify(booksService).findWithPage(page, bookPerPage, sortByYear);
    }

    @Test
    public void testShow(){
        Person person = new Person("Test Person", 2000);
        when(booksService.findOne(id)).thenReturn(book);
        when(booksService.getBookOwner(id)).thenReturn(null);

        String result = booksController.show(id, model, person);
        assertEquals("books/show", result);
        verify(model).addAttribute(eq("people"), anyList());

        when(booksService.getBookOwner(id)).thenReturn(person);

        result = booksController.show(id, model, person);
        assertEquals("books/show", result);
        verify(model).addAttribute("owner", person);

    }

    @Test
    public void testNewBook(){
        String result = booksController.newBook(book);
        assertEquals("books/new", result);
    }

    @Test
    public void testCreate(){
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = booksController.create(book, bindingResult);
        assertEquals("redirect:/books", result);

        when(bindingResult.hasErrors()).thenReturn(true);
        result = booksController.create(book, bindingResult);
        assertEquals("books/new", result);
    }

    @Test
    public void testEdit(){
        when(booksService.findOne(id)).thenReturn(book);

        String result = booksController.edit(model, id);
        assertEquals("books/edit", result);
    }

    @Test
    public void testUpdate(){
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = booksController.update(book, id, bindingResult);
        assertEquals("redirect:/books", result);

        when(bindingResult.hasErrors()).thenReturn(true);
        result = booksController.update(book, id, bindingResult);
        assertEquals("books/edit", result);
    }

    @Test
    public void testDelete(){
        String result = booksController.delete(id);
        assertEquals("redirect:/books", result);
    }

    @Test
    public void testRelease(){
        String result = booksController.release(id);
        assertEquals("redirect:/books/" +id, result);
    }

    @Test
    public void testAssign(){
        Person person = new Person("Test Person", 2000);
        String result = booksController.assign(id, person);
        assertEquals("redirect:/books/"+id, result);
    }

    @Test
    public void testSearchMethod(){
        String result = booksController.searchMethod();
        assertEquals("books/search", result);
    }

    @Test
    public void testSearch(){
        String startOfTitle = "The";
        String result = booksController.search(model, startOfTitle);
        assertEquals("books/search", result);
    }
}
