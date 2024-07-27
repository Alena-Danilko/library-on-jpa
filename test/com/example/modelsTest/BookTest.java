package com.example.modelsTest;

import com.example.models.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    private Validator validator;

    @BeforeEach
    public void setUp(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testTitleValidation(){
        Book book = new Book();
        book.setTitle(null);
        book.setAuthor("Bulgakov M.");
        book.setYearOfPublication(1967);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertEquals("Title shouldn't be empty", violations.iterator().next().getMessage());

        book.setTitle("Master");
        violations = validator.validate(book);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testAuthorValidation(){
        Book book = new Book();
        book.setTitle("Master");
        book.setAuthor(null);
        book.setYearOfPublication(1967);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertEquals("Author shouldn't be empty", violations.iterator().next().getMessage());

        book.setAuthor("B");
        violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertEquals("The name of author should be in between 2 to 200 characters", violations.iterator().next().getMessage());

        book.setAuthor("Bu");
        violations = validator.validate(book);
        assertTrue(violations.isEmpty());

        book.setAuthor("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB" +
                "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        violations = validator.validate(book);
        assertTrue(violations.isEmpty());

        book.setAuthor("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB" +
                "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertEquals("The name of author should be in between 2 to 200 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void testYearOfPublicationValidation(){
        Book book = new Book();
        book.setTitle("Master");
        book.setAuthor("Bulgakov M.");
        book.setYearOfPublication(-1800);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertEquals("Year of publication should be greater than 0", violations.iterator().next().getMessage());

        book.setYearOfPublication(0);
        violations = validator.validate(book);
        assertTrue(violations.isEmpty());
    }
}
