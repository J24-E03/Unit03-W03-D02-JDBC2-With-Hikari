package com.omar;

import java.time.LocalDate;

public class Movie {
    private int movieId;
    private String title;
    private int releaseYear;
    private int duration;
    private String language;
    private double rating;
    private Director director;
    private int genreId;

    public Movie(int movieId, String title, int releaseYear, int duration, String language, double rating, Director director, int genreId) {
        this.movieId = movieId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.language = language;
        this.rating = rating;
        this.director = director;
        this.genreId = genreId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                ", language='" + language + '\'' +
                ", rating=" + rating +
                ", director=" + director +
                ", genreId=" + genreId +
                '}';
    }
}
