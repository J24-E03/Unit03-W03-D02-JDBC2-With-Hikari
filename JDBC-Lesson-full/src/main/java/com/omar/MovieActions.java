package com.omar;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieActions {

    private static final HikariDataSource dataSource;


//    setting up our hikari connection pool
    static{
//        initializing the connection pool
        HikariConfig config = new HikariConfig();

//        1. hardcoding (bad practice):
//        config.setJdbcUrl("jdbc:postgresql://localhost:5432/movies_db");
//        config.setUsername("postgres");
//        config.setPassword("1234");


//        2. Adding our url, username, password to the environment variables:
/*

           Windows:
               setx DB_URL "jdbc:postgresql://localhost:5432/movies_db"
               setx DB_USER "postgres"
               setx DB_PASSWORD "1234"
           Linux Or Mac:
               export DB_URL="jdbc:postgresql://localhost:5432/movies_db"
               export DB_USER="postgres"
               export DB_PASSWORD="your_secure_password"

    */

    String url = System.getenv("DB_URL");
    String username = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");


    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);

    

//        set the connection pool size
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }


//    fetches a connection from the connection pool
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


//    public List<Movie> getAllMovies() {
//
////        Statement statement = null;
////        ResultSet resultSet = null;
//        String query = "SELECT * FROM movies ORDER BY title DESC";
//
//        try(Connection connection = getConnection();Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query);) {
//
////            creates a statement to make the query
//
////            execute the query using the statement
//
//
//
//            ArrayList<Movie> movies = new ArrayList<>();
//
//            while (resultSet.next()) {
//                movies.add(new Movie(
//                        resultSet.getInt("movie_id"),
//                        resultSet.getString("title"),
//                        resultSet.getInt("release_year"),
//                        resultSet.getInt("duration"),
//                        resultSet.getString("language"),
//                        resultSet.getDouble("rating"),
//                        resultSet.getInt("director_id"),
//                        resultSet.getInt("genre_id")
//                ));
//
//            }
//            return movies;
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        finally{
////            if(resultSet != null) resultSet.close();
////            if(statement != null) statement.close();
//        }


//    }

//    pulp fiction
//    1994
//    DROP TABLE movies;

    //    correct way of inserting
    public void insertMovie(String title, int releaseYear, double rating){


        String query = "INSERT INTO movies(title,release_year,rating) VALUES(?, ?, ?)";
//        String query = String.format("INSERT INTO movies(rating,release_year,title) VALUES(%s,%d,%f)",title,releaseYear,rating);

//        try with resources automatically closes my prepared statement
        try(Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(query)){

//            protects against sql injections
            preparedStatement.setString(1,title);
            preparedStatement.setInt(2,releaseYear);
            preparedStatement.setDouble(3,rating);

            int rowsAdded = preparedStatement.executeUpdate();

            System.out.println("Rows inserted " + rowsAdded);
        }catch (SQLException e){
            System.out.println(e);
        }
    }

//    incorrect way of inserting
//    public static void insertMovieUnsafe(Connection connection, String title, int releaseYear, double rating){
//        String query = String.format("INSERT INTO movies(title,release_year,rating) VALUES('%s',%d,%f)",title,releaseYear,rating);
//
//        System.out.println(query);
//
//        try(Statement statement = connection.createStatement()){
//            int rowsAdded = statement.executeUpdate(query);
//            System.out.println("Rows added " + rowsAdded);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    //    Unsafe way of executing inserts
    public void insertMovieUnsafe( String title, int releaseYear, double rating) {
        String query = "INSERT INTO movies (rating, release_year, title) VALUES (" + rating + ", " + releaseYear + ", '" + title + "')";


        try (Connection connection = getConnection();Statement stmt = connection.createStatement()) {
            int rowsAdded = stmt.executeUpdate(query);
            System.out.println("Rows inserted: " + rowsAdded);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }


    public void getMovieStartingWith(String startingChar){
        String query = "SELECT * FROM movies WHERE title ILIKE ?";

        try(Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, startingChar + "%");
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getString("title"));
                }
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    public void updateMovieById(int movieId, double rating, String title){
        String query = """
                        UPDATE movies
                        SET rating = ?, title = ?
                        WHERE movie_id = ?;
                        """;
        try(Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setDouble(1,rating);
            preparedStatement.setString(2,title);
            preparedStatement.setInt(3,movieId);

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Rows affected by Update " + rowsUpdated);

        } catch (SQLException e) {
            System.out.println(e);;
        }
    }

    public void deleteMovieById(int movieId){
        String query = "DELETE FROM movies WHERE movie_id = ?";

        try(Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,movieId);
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted by query " + rowsDeleted);

        }
        catch (SQLException e) {
            System.out.println(e);;
        }
    }


    public Movie getMovieByIdWithDirector(int id){
        String query = """
                
                        SELECT *  from movie_directors
                        WHERE movie_id = ?;
                """;

        try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return new Movie(
                        resultSet.getInt("movie_id"),
                        resultSet.getString("title"),
                        resultSet.getInt("release_year"),
                        resultSet.getInt("duration"),
                        resultSet.getString("language"),
                        resultSet.getDouble("rating"),
                        new Director(
                                resultSet.getInt("director_id"),
                                resultSet.getString("director_first_name"),
                                resultSet.getString("director_last_name")
                        ),
                        resultSet.getInt("genre_id")
                );
            }

        }catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public static void closeDataSource(){
        dataSource.close();
    }
}
