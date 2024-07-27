package com.example.serviceTest;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.PeopleRepository;
import com.example.service.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PeopleServiceTest {
    @Mock
    private PeopleRepository peopleRepository;
    private PeopleService peopleService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        peopleService = new PeopleService(peopleRepository);
    }

    @Test
    public void testFindAll(){
        List<Person> expectedPeople = new ArrayList<>();
        expectedPeople.add(new Person("Jake Jim", 1999));
        expectedPeople.add(new Person("Jake Jo", 1998));

        when(peopleRepository.findAll()).thenReturn(expectedPeople);

        List<Person> foundPeople = peopleService.findAll();
        assertFalse(foundPeople.isEmpty());
        assertEquals(2, foundPeople.size());
        assertEquals(expectedPeople, foundPeople);
    }

    @Test
    public void testFindOne(){
        int id = 1;
        Person expectedPerson = new Person("Jake Jim", 1999);
        when(peopleRepository.findById(id)).thenReturn(Optional.of(expectedPerson));

        Person foundPerson = peopleService.findOne(id);
        assertNotNull(foundPerson);
        assertEquals(expectedPerson, foundPerson);
    }

    @Test
    public void testSavePerson(){
        Person person = new Person("Jake Jim", 1999);
        when(peopleRepository.save(any(Person.class))).thenReturn(person);
        peopleService.save(person);
        verify(peopleRepository).save(person);

        ArgumentCaptor<Person> result = ArgumentCaptor.forClass(Person.class);
        verify(peopleRepository).save(result.capture());
        Person savedPerson = result.getValue();

        assertEquals("Jake Jim", savedPerson.getFullName());
        assertEquals(1999, savedPerson.getYearOfBirth());
    }

    @Test
    public void testUpdatePerson(){
        int id = 1;
        Person updatedPerson = new Person("Jake Jim", 1999);
        peopleService.update(1, updatedPerson);

        assertEquals(id, updatedPerson.getId());
        verify(peopleRepository).save(updatedPerson);
    }

    @Test
    public void testDeletePerson(){
        int id = 1;
        Person deletedPerson = new Person("Jake Jim", 1999);
        deletedPerson.setId(id);

        when(peopleRepository.findById(id)).thenReturn(Optional.of(deletedPerson));
        peopleService.delete(1);
        verify(peopleRepository).deleteById(1);

        when(peopleRepository.findById(id)).thenReturn(Optional.empty());
        assertFalse(peopleRepository.findById(id).isPresent());
    }

    @Test
    public void testGetPersonByFullName(){
        Optional<Person> opt = Optional.of(new Person("Jake Jim", 1999));
        when(peopleRepository.findByFullName("Jake Jim")).thenReturn(opt);

        Optional<Person> result = peopleRepository.findByFullName("Jake Jim");
        assertNotNull(result);
        assertEquals("Jake Jim", result.get().getFullName());
    }

    @Test
    public void testGetBookByPersonId(){
        int id =1;
        Person person = new Person("Jake Jim", 1999);
        Book book1 = new Book("The Master and Margarita", "Bulgakov Mikhail", 1967);
        book1.setTakenIn(new Date(System.currentTimeMillis() - 865000000));
        Book book2 = new Book("The Master", "Bulgakov Mikhail", 1969 );
        book2.setTakenIn(new Date(System.currentTimeMillis() - 3000));
        person.setBooks(Arrays.asList(book1, book2));

        when(peopleRepository.findById(id)).thenReturn(Optional.of(person));
        List<Book> foundBooks = peopleService.getBookByPersonId(id);

        assertEquals(2, foundBooks.size());
        assertTrue(foundBooks.get(0).isExpired());
        assertFalse(foundBooks.get(1).isExpired());
    }
}
