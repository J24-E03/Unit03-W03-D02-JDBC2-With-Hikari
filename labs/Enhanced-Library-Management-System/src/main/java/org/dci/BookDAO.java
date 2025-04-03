package org.dci;

import com.zaxxer.hikari.HikariDataSource;
import org.dci.domain.Author;
import org.dci.domain.Book;
import org.dci.utils.HikariCPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final Logger logger = LoggerFactory.getLogger(BookDAO.class);
    public static final HikariDataSource dataSource = HikariCPConfig.getDataSource();
    private final AuthorDAO authorDAO = new AuthorDAO();

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        String query = "SELECT * FROM books";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();) {

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                Author author = authorDAO.getAuthorById(authorId);

                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        author,
                        resultSet.getInt("publication_year"),
                        resultSet.getBoolean("is_available")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    public boolean addBook(String title, Author author, int publicationYear, boolean isAvailable ) {
        String query = """
                INSERT INTO books (title, author_id, publication_year, is_available)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, author.getAuthorId());
            preparedStatement.setInt(3, publicationYear);
            preparedStatement.setBoolean(4, isAvailable);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;


        } catch (SQLException e) {
            logger.error("Error adding new book: ", e);
            return false;
        }

    }

    public boolean updateBook(int bookId, String newTitle, int newPublicationYear, boolean isAvailable) {
        String query = """
                UPDATE books 
                SET title = ?, publication_year = ?, is_available = ? 
                WHERE book_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newTitle);
            preparedStatement.setInt(2, newPublicationYear);
            preparedStatement.setBoolean(3, isAvailable);
            preparedStatement.setInt(4, bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.error("Error updating book: ", e);
            return false;
        }
    }

    public boolean deleteBook(int bookId) {
        String query = "DELETE FROM books WHERE book_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.error("Error deleting book: ", e);
            return false;
        }
    }

    public List<Book> retrieveBookByAuthor(String authorName) {
        List<Book> books = new ArrayList<>();
        Author author = authorDAO.getAuthorName(authorName);
        if (author == null) {
            return books;
        }

        String query = """
                SELECT b.*, a.*
                FROM books b
                JOIN authors a ON b.author_id = a.author_id
                WHERE author_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, author.getAuthorId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        author,
                        resultSet.getInt("publication_year"),
                        resultSet.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            logger.error("Error fetching books: ", e);

        }
        return books;
    }

    public List<Book> retrieveBookByBestSellerAuthors() {
        List<Book> books = new ArrayList<>();

        String query = """
                SELECT b.*
                FROM books b
                JOIN authors a ON b.author_id = a.author_id
                WHERE a.is_best_seller = TRUE
                """;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int authorID = resultSet.getInt("author_id");
                Author author = authorDAO.getAuthorById(authorID);
                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        author,
                        resultSet.getInt("publication_year"),
                        resultSet.getBoolean("is_available")
                ));
            }

        } catch (SQLException e) {
            logger.error("Error fetching books: ", e);

        }
        return books;
    }

    // Bonus Tasks
    public List<Book> getAllBooksPaginated(int limit, int offset) {
        List<Book> books = new ArrayList<>();

        String query = """
                SELECT *
                FROM books
                LIMIT ? OFFSET ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                Author author = authorDAO.getAuthorById(authorId);

                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        author,
                        resultSet.getInt("publication_year"),
                        resultSet.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            logger.error("Error fetching paginated books: ", e);
        }
        return books;
    }

    public List<Book> getSortedBooks(String sortBy, String sortOrder) {
        List<Book> books = new ArrayList<>();

        List<String> validSortColumns = List.of("title", "publication_year", "is_available");
        if (!validSortColumns.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sorting column!");
        }
        if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Invalid sorting order!");
        }

        String query = "SELECT * FROM books ORDER BY " + sortBy + " " + sortOrder;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                Author author = authorDAO.getAuthorById(authorId);

                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        author,
                        resultSet.getInt("publication_year"),
                        resultSet.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            logger.error("Error fetching sorted books: ", e);
        }
        return books;
    }

    public List<Book> advancedSearch(String title, String authorName, Integer minYear, Integer maxYear) {
        List<Book> books = new ArrayList<>();
        String query = """
                SELECT * 
                FROM books
                WHERE title ILIKE ? AND author_id IN (
                SELECT author_id FROM authors WHERE author_name ILIKE ?
                ) AND publication_year BETWEEN ? AND ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + (title != null ? title : "") + "%");
            statement.setString(2, "%" + (authorName != null ? authorName : "") + "%");
            statement.setInt(3, minYear != null ? minYear : Integer.MIN_VALUE);
            statement.setInt(4, maxYear != null ? maxYear : Integer.MAX_VALUE);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                Author author = authorDAO.getAuthorById(authorId);

                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        author,
                        resultSet.getInt("publication_year"),
                        resultSet.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            logger.error("Error performing advanced search: ", e);
        }
        return books;
    }

}
