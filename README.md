# LibraryJPABoot


### Description:
This project is rewrite of the [Library](https://github.com/git-alexbondarenko/Library) project using Spring Boot, Hibernate and Spring DataJPA, with some additional functionality.


### New functionality:
1. Added pagination for books. There may be many books and they may not fit on one page in browser. To solve this problem, the controller method needs to be able to give out not only all the books at once, but also break the issue into pages.
2. Added sorting books by year. Pagination and sorting implemented using GET request parameters.
3. Created a book search page. Enter initial letters of title of the book, user gets the full title of book and the name of the author. Also, if the book is now with someone, name of this person is shown.
4. Added automatic check that the person has overdue the return books.


### Stack used:
Spring MVC, Spring DataJPA, Spring Boot, Thymeleaf, PostgreSQL
