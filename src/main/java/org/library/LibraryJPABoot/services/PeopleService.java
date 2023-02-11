package org.library.LibraryJPABoot.services;

import org.library.LibraryJPABoot.models.Book;
import org.library.LibraryJPABoot.models.Person;
import org.library.LibraryJPABoot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> booksBorrowed(int personId) {
        Person person = findOne(personId);
        List<Book> booksBorrowed = person.getBooksBorrowed();
        for (Book book: booksBorrowed) {
            book.setOverdue(isOverdue(book));
        }
        return booksBorrowed;
    }

    public boolean isOverdue(Book book) {
        Date dateNow = new Date();
        if (book.getTimestamp() != null) {
            long diffInMillis = dateNow.getTime() - book.getTimestamp().getTime();
            long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return diff >= 10;
        }
        return false;
    }

    public Optional<Person> findByName(String name) {
        return peopleRepository.findByName(name);
    }

}
