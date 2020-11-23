package com.webapp.services;

import com.webapp.models.Movie;

import java.util.List;
import java.util.Set;

public interface MovieService {

    List<Movie> findAll();

    List<Movie> getLatestMovies();

    Set<Movie> getMoviesByIds(Set<String> movieIds);

    Movie addMovie(Movie movie);

    void addMultipleMovies(List<Movie> movies);

    Movie findById(String id);

    Movie updateMovie(String id, Movie movie);

    void deleteMovie(String id);

}
