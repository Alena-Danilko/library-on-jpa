package com.example.modelsTest;

import com.example.models.Person;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private Validator validator;

    @BeforeEach
    public void setUp(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testFullName(){
        Person person = new Person();
        person.setFullName(null);
        person.setYearOfBirth(1999);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals("Full name shouldn't be empty", violations.iterator().next().getMessage());

        person.setFullName("K");
        violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals("Full name should be in between 2 to 200 characters", violations.iterator().next().getMessage());

        person.setFullName("Ki");
        violations = validator.validate(person);
        assertTrue(violations.isEmpty());

        person.setFullName("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK" +
                "KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        violations = validator.validate(person);
        assertTrue(violations.isEmpty());

        person.setFullName("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK" +
                "KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals("Full name should be in between 2 to 200 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void testYearOfBirth(){
        Person person = new Person();
        person.setFullName("Kiril K");
        person.setYearOfBirth(1800);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals("Year of birth should be greater than 1900", violations.iterator().next().getMessage());

        person.setYearOfBirth(1900);
        violations = validator.validate(person);
        assertTrue(violations.isEmpty());
    }
}
