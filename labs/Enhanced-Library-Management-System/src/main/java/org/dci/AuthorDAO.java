package org.dci;

import com.zaxxer.hikari.HikariDataSource;
import org.dci.domain.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AuthorDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuthorDAO.class);
    public static final HikariDataSource dataSource = org.dci.utils.HikariCPConfig.getDataSource();

    public Author getAuthorById(int authorId) {
        String query = "SELECT * FROM authors WHERE author_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, authorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Author(
                        rs.getInt("author_id"),
                        rs.getString("author_name"),
                        rs.getInt("books_sold"),
                        rs.getBoolean("is_best_seller")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Author getAuthorName(String authorName) {
        String query = "SELECT * FROM authors WHERE author_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, authorName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Author(
                        rs.getInt("author_id"),
                        rs.getString("author_name"),
                        rs.getInt("books_sold"),
                        rs.getBoolean("is_best_seller")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}