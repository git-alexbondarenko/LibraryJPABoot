package org.library.LibraryJPABoot.services;

import org.library.LibraryJPABoot.models.Book;
import org.library.LibraryJPABoot.models.Person;
import org.library.LibraryJPABoot.repositories.BooksRepository;
import org.library.LibraryJPABoot.repositories.PeopleRepository;
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
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(boolean sort) {
        return booksRepository.findAll(Sort.by("year"));
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sort) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> foundPerson = booksRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedPerson) {
        updatedPerson.setId(id);
        booksRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }


    public List<Book> booksBorrowedByPerson(int id) {
        if (peopleRepository.findById(id).isPresent()) {
            return peopleRepository.findById(id).get().getBooksBorrowed();
        } else {
            return null;
        }
    }

    @Transactional
    public void borrowBook(Person person, int id) {
        Book bookToBorrow = booksRepository.getReferenceById(id);
        bookToBorrow.setBorrower(person);
        bookToBorrow.setTimestamp(new Date());
    }

    @Transactional
    public void returnBook(int id) {
        Book bookToReturn =booksRepository.getReferenceById(id);
        bookToReturn.setBorrower(null);
        bookToReturn.setTimestamp(null);
    }

    public Person getBorrower(int id) {
        if (booksRepository.findById(id).isPresent()) {
            return booksRepository.findById(id).get().getBorrower();
        } else {
            return null;
        }
    }

    public List<Book> searchBook(String t) {
        List<Book> books = booksRepository.findByTitleStartingWith(t);
        return books;
    }
}
