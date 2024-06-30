package com.example.service;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear){
        if(sortByYear){
            return booksRepository.findAll(Sort.by("yearOfPublication"));
        } else {
            return booksRepository.findAll();
        }
    }
    public List<Book> findWithPage(Integer page, Integer bookPerPage, boolean sortByYear) {
        if(sortByYear){
            return booksRepository.findAll(PageRequest.of(page, bookPerPage, Sort.by("yearOfPublication"))).getContent();
        } else{
            return booksRepository.findAll(PageRequest.of(page, bookPerPage)).getContent();
        }

    }
    public Book findOne(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenIn(null);
        });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setTakenIn(new Date());
            book.setOwner(selectedPerson);
        });
    }

    @Transactional
    public List<Book> searchByTitle(String startOfTitle) {
        return booksRepository.findByTitleStartingWith(startOfTitle);
    }
}
