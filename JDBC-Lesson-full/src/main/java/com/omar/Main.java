package com.omar;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        MovieActions movieActions = new MovieActions();

//        Connection connection = null;

//        close: connection, statement, resultSet, preparedStatement

//            establishing the connection using our driver

//        List<Movie> movies = movieActions.getAllMovies();
//        movies.forEach(System.out::println);

//            CRUD Operations
//            CREATE, READ, UPDATE, DELETE


//            READ: all movies

//            List<Movie> movies = movieActions.getAllMovies(connection);

//            movies.forEach(System.out::println);

//            CREATE: add 1 movie to our table
//            insertMovie(connection,);

//            Example of SQL injection:
//            movieActions.insertMovieUnsafe(connection,"'); DROP TABLE movies CASCADE; --",2025,8.5);

//            getMovieStartingWith(connection,"T");


//            UPDATE: update movie by Id
//
//            movieActions.updateMovieById(29,9,"New Updated movie");

        System.out.println(movieActions.getMovieByIdWithDirector(1));


//        System.out.println(System.getenv("DB_USER"));


//            DELETE: delete movie by id
//            movieActions.deleteMovieById(connection,30);


//        MovieActions.closeDataSource();









//            making queries



    }


}