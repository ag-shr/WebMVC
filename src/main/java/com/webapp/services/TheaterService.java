package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.models.Theater;
import com.webapp.models.Theater.ShortMovie;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TheaterService {

    Theater findTheaterById(String id);

//    List<ShortMovie> getMovies(String id);
//
//    List<Theater> getAllTheaters();

    Theater addTheater(Theater theater);

    void removeTheater(String theaterId);

    Theater updateTheater(String id, Theater theater);

    Theater addMovieInTheater(String theaterId, String movieId);

    void removeMovieFromTheater(String theaterId, String movieId);

//    void removeTheseMoviesFromTheseTheaters(Map<String, Set<String>> map);
//
//    List<Theater> getTheatersInCity(String cityId);
//
//    Set<ShortMovie> getMoviesInCity(String cityId);

    List<Movie> getFullMoviesInCity(String cityId);

    List<Theater> getTheatersRunningThisMovie(String cityId, String movieId);

//    Boolean validateTheaterAndMovie(String theaterId, String movieId);
//
//    Map<String, String> getCitiesByIds(List<String> cityIds);
//
//    void removeTheatersFromCity(String cityId);
//
//    void addMultipleTheaters(List<Theater> theaters);
//
//    Map<String, String> getCitiesByIds(List<String> cityIds);

    void addMultipleTheaters(List<Theater> theaters, String cityId);
}
