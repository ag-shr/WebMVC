package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.models.Theater;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {

    @Override
    public Theater findTheaterById(String id) {
        return null;
    }

    @Override
    public Theater addTheater(Theater theater) {
        return null;
    }

    @Override
    public void removeTheater(String theaterId) {

    }

    @Override
    public Theater updateTheater(String id, Theater theater) {
        return null;
    }

    @Override
    public Theater addMovieInTheater(String theaterId, String movieId) {
        return null;
    }

    @Override
    public void removeMovieFromTheater(String theaterId, String movieId) {

    }

    @Override
    public List<Movie> getFullMoviesInCity(String cityId) {
        return null;
    }

    @Override
    public List<Theater> getTheatersRunningThisMovie(String cityId, String movieId) {
        return null;
    }

}
