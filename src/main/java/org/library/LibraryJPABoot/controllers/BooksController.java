package org.library.LibraryJPABoot.controllers;

import jakarta.validation.Valid;
import org.library.LibraryJPABoot.models.Book;
import org.library.LibraryJPABoot.models.Person;
import org.library.LibraryJPABoot.services.BooksService;
import org.library.LibraryJPABoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false) String sortByYear) {
        if (booksPerPage != null && Objects.equals(sortByYear, "true")) {
            model.addAttribute("books", booksService.findAll(page, booksPerPage));
        } else if (Objects.equals(sortByYear, "true")) {
            model.addAttribute("books", booksService.findAll(true));
        } else if (booksPerPage != null) {
            model.addAttribute("books", booksService.findAll(page, booksPerPage, true));
        } else {
            model.addAttribute("books", booksService.findAll());
        }
        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute("person") Person person, @PathVariable("id") int id, Model model) {
        Book book = booksService.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("booksTaken", booksService.booksBorrowedByPerson(id));
        if (book.getBorrower() != null) {
            model.addAttribute("owner", booksService.getBorrower(id));
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "/books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book) {
        return "/books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "/books/edit";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/take")
    public String takeBook(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        booksService.borrowBook(person, id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult, @PathVariable("id") int id) {
        booksService.returnBook(id);
        return "redirect:/books/{id}";
    }

    @Transactional
    @GetMapping("/search")
    public String searchPage(@RequestParam(name = "title", required = false) String title, Model model) {
        if (title != null) {
            model.addAttribute("booksFound",booksService.searchBook(title));
        }
        return "/books/search";
    }
}
