package com.example.utilTest;

import com.example.models.Person;
import com.example.service.PeopleService;
import com.example.util.PersonValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PersonValidatorTest {
    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private PersonValidator personValidator;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateTakenName(){
        Person person = new Person("Test Person", 2000);
        Person newPerson = new Person("Test Person", 2003);
        when(peopleService.getPersonByFullName("Test Person")).thenReturn(Optional.of(person));

        Errors errors = new BeanPropertyBindingResult(newPerson, "person");
        personValidator.validate(newPerson, errors);

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("Человек с таким именем уже существует", errors.getFieldError("fullName").getDefaultMessage());
    }

    @Test
    public void testValidateNameIsNotTaken(){
        Person person = new Person("Test Person", 1998);
        when(peopleService.getPersonByFullName("Test Person")).thenReturn(Optional.empty());

        Errors errors = new BeanPropertyBindingResult(person, "person");
        personValidator.validate(person, errors);

        assertFalse(errors.hasErrors());
        assertEquals(0, errors.getFieldErrors().size());
    }
}
