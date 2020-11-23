package com.webapp.services;

import com.webapp.models.Movie;

public interface MovieService {

    Movie addMovie(Movie movie);

    Movie findById(String id);

    Movie updateMovie(String id, Movie movie);

    void deleteMovie(String id);

}
