package com.example.controllersTest;

import com.example.controllers.PeopleController;
import com.example.models.Book;
import com.example.models.Person;
import com.example.service.PeopleService;
import com.example.util.PersonValidator;
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
import static org.mockito.Mockito.when;

public class PeopleControllerTest {
    @Mock
    private PeopleService peopleService;

    @Mock
    private PersonValidator personValidator;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PeopleController peopleController;

    private int id;
    private Person person;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        id = 1;
        person = new Person("Test Person", 2000);
    }

    @Test
    public void testIndex(){
        List<Person> people = new ArrayList<>();
        when(peopleService.findAll()).thenReturn(people);

        String result = peopleController.index(model);
        assertEquals("people/index", result);
    }

    @Test
    public void testShow(){
        List<Book> books = new ArrayList<>();

        when(peopleService.findOne(id)).thenReturn(person);
        when(peopleService.getBookByPersonId(id)).thenReturn(books);

        String result = peopleController.show(id, model);
        assertEquals("people/show", result);
    }

    @Test
    public void testNewPerson(){
        String result = peopleController.newPerson(person);

        assertEquals("people/new", result);
    }

    @Test
    public void testCreate(){
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = peopleController.create(person, bindingResult);
        assertEquals("redirect:/people", result);

        when(bindingResult.hasErrors()).thenReturn(true);
        result = peopleController.create(person, bindingResult);
        assertEquals("people/new", result);
    }

    @Test
    public void testEdit(){
        when(peopleService.findOne(id)).thenReturn(person);

        String result = peopleController.edit(model, id);
        assertEquals("people/edit", result);
    }

    @Test
    public void testUpdate(){
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = peopleController.update(person, bindingResult, id);
        assertEquals("redirect:/people", result);

        when(bindingResult.hasErrors()).thenReturn(true);
        result = peopleController.update(person, bindingResult, id);
        assertEquals("people/edit", result);
    }

    @Test
    public void testDelete(){
        String result = peopleController.delete(id);

        assertEquals("redirect:/people", result);
    }
}
