package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.models.Theater;

import java.util.List;

public interface TheaterService {

    Theater findTheaterById(String id);

    Theater addTheater(Theater theater);

    void removeTheater(String theaterId);

    Theater updateTheater(String id, Theater theater);

    Theater addMovieInTheater(String theaterId, String movieId);

    void removeMovieFromTheater(String theaterId, String movieId);

    List<Movie> getFullMoviesInCity(String cityId);

    List<Theater> getTheatersRunningThisMovie(String cityId, String movieId);

}
