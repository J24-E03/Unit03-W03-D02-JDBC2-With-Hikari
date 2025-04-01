package org.dci;

import org.dci.domain.Author;
import org.dci.domain.Book;
import org.dci.utils.HikariCPConfig;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HikariCPConfig.initialize("library_db_lab");

        AuthorDAO authorDAO = new AuthorDAO();
        BookDAO bookDAO = new BookDAO();
        Scanner scanner = new Scanner(System.in);

        Author author = authorDAO.getAuthorName("J.K. Rowling");
        if (author == null) {
            System.out.println("Author not found.");
            return;
        }

        boolean bookAdded = bookDAO.addBook("Harry Potter and the Goblet of Fire", author, 2000, true);
        System.out.println("Book added: " + bookAdded);


        List<Book> allBooks = bookDAO.getAllBooks();
        System.out.println("\nAll Books:");
        allBooks.forEach(System.out::println);

        if (!allBooks.isEmpty()) {
            Book bookToUpdate = allBooks.get(0);
            boolean updated = bookDAO.updateBook(bookToUpdate.getBookId(), "Updated Title", 2024, false);
            System.out.println("Book updated: " + updated);
        }


        List<Book> bestSellerBooks = bookDAO.retrieveBookByBestSellerAuthors();
        System.out.println("\nBooks by Best-Selling Authors:");
        bestSellerBooks.forEach(System.out::println);

        if (!allBooks.isEmpty()) {
            int bookIdToDelete = allBooks.get(0).getBookId();
            boolean deleted = bookDAO.deleteBook(bookIdToDelete);
            System.out.println("Book deleted: " + deleted);
        }

        System.out.println("\nAll Books After Deletion:");
        bookDAO.getAllBooks().forEach(System.out::println);

        System.out.println("\nFetch All Books with Pagination");
        System.out.print("Enter page number: ");
        int page = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter page size: ");
        int pageSize = scanner.nextInt();
        scanner.nextLine();
        List<Book> paginatedBooks = bookDAO.getAllBooksPaginated(page, pageSize);
        paginatedBooks.forEach(System.out::println);


        System.out.println("\nFetch All Books with Dynamic Sorting");
        System.out.print("Enter sorting field (title, publication_year, is_available): ");
        String sortBy = scanner.nextLine();
        System.out.print("Enter sorting order (asc/desc): ");
        String sortOrder = scanner.nextLine();
        List<Book> sortedBooks = bookDAO.getSortedBooks(sortBy, sortOrder);
        sortedBooks.forEach(System.out::println);

        System.out.println("\nAdvanced Search");
        System.out.print("Enter title keyword (or press enter to skip): ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter author name (or press enter to skip): ");
        String authorName = scanner.nextLine().trim();
        System.out.print("Enter min publication year (or 0 to skip): ");
        int minYear = scanner.nextInt();
        System.out.print("Enter max publication year (or 0 to skip): ");
        int maxYear = scanner.nextInt();

        List<Book> searchResults = bookDAO.advancedSearch(
                title.isEmpty() ? null : title,
                authorName.isEmpty() ? null : authorName,
                minYear == 0 ? null : minYear,
                maxYear == 0 ? null : maxYear
        );

        System.out.println("\nSearch Results");
        searchResults.forEach(System.out::println);

        scanner.close();

    }
}