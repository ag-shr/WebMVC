package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.models.Theater;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TheaterServiceImpl implements TheaterService {

    @Override
    public Theater findTheaterById(String id) {
        return null;
    }

    @Override
    public List<Theater.ShortMovie> getMovies(String id) {
        return null;
    }

    @Override
    public List<Theater> getAllTheaters() {
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
    public void removeTheseMoviesFromTheseTheaters(Map<String, Set<String>> map) {

    }

    @Override
    public List<Theater> getTheatersInCity(String cityId) {
        return null;
    }

    @Override
    public Set<Theater.ShortMovie> getMoviesInCity(String cityId) {
        return null;
    }

    @Override
    public List<Movie> getFullMoviesInCity(String cityId) {
        return null;
    }

    @Override
    public List<Theater> getTheatersRunningThisMovie(String cityId, String movieId) {
        return null;
    }

    @Override
    public Boolean validateTheaterAndMovie(String theaterId, String movieId) {
        return null;
    }

    @Override
    public void removeTheatersFromCity(String cityId) {

    }

    @Override
    public void addMultipleTheaters(List<Theater> theaters) {

    }

    @Override
    public Map<String, String> getCitiesByIds(List<String> cityIds) {
        return null;
    }

    @Override
    public void addMultipleTheaters(List<Theater> theaters, String cityId) {

    }
}
