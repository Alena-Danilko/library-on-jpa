package com.example.repositoriesTest;

import com.example.models.Person;
import com.example.repositories.PeopleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PeopleRepositoryTest {
    @Mock
    private PeopleRepository peopleRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByFullNameSuccessfully(){
        Optional<Person> opt = Optional.of(new Person("Jake Jim", 1999));

        when(peopleRepository.findByFullName("Jake Jim")).thenReturn(opt);

        Optional<Person> foundPerson = peopleRepository.findByFullName("Jake Jim");
        assertTrue(foundPerson.isPresent());
        assertEquals("Jake Jim", foundPerson.get().getFullName());
    }

    @Test
    public void testFindByFullNameUnsuccessfully(){
        Optional<Person> opt = Optional.of(new Person("Jake Jim", 1999));

        when(peopleRepository.findByFullName("Jake Jim")).thenReturn(opt);

        Optional<Person> foundPerson = peopleRepository.findByFullName("Jim");
        assertFalse(foundPerson.isPresent());
    }
}
