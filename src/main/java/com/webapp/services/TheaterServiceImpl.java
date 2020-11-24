package com.webapp.services;

import com.webapp.models.City;
import com.webapp.models.Movie;
import com.webapp.models.Theater;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {

    private final String theaterBaseUrl = "http://localhost:8081/v1/theaters/";
    private final String locationBaseUrl = "http://localhost:8081/v1/cities/";
    private static final String NO_BODY = "No Body";


    @Override
    public Theater findTheaterById(String id) {
        String url = theaterBaseUrl + id;
        return (Theater) ServiceCallUtil.getForEntity(url, Theater.class);
    }

    @Override
    public Theater addTheater(Theater theater) {
        return (Theater) ServiceCallUtil.postPutForEntity(theaterBaseUrl, HttpMethod.POST, Theater.class, Theater.class, theater);
    }

    @Override
    public void removeTheater(String theaterId) {
        var url = theaterBaseUrl + theaterId;
        ServiceCallUtil.delete(url);
    }

    @Override
    public Theater updateTheater(String id, Theater theater) {
        var url = theaterBaseUrl + id.trim();
        return (Theater) ServiceCallUtil.postPutForEntity(url, HttpMethod.PUT, Theater.class, Theater.class, theater);
    }

    @Override
    public Theater addMovieInTheater(String theaterId, String movieId) {
        var url = theaterBaseUrl + theaterId + "/movies/" + movieId;
        return (Theater) ServiceCallUtil.postPutForEntity(url, HttpMethod.POST, Theater.class, Theater.class, NO_BODY);
    }

    @Override
    public void removeMovieFromTheater(String theaterId, String movieId) {
        var url = theaterBaseUrl + theaterId + "/movies/" + movieId;
        ServiceCallUtil.delete(url);
    }

    @Override
    public List<Movie> getFullMoviesInCity(String cityId) {
        var url = locationBaseUrl + cityId + "/movies/details";
        return (List<Movie>) ServiceCallUtil.getForList(url, Movie.class);
    }

    @Override
    public List<Theater> getTheatersRunningThisMovie(String cityId, String movieId) {
        var url = locationBaseUrl + cityId + "/theaters/" + movieId;
        return (List<Theater>) ServiceCallUtil.getForList(url, Theater.class);
    }

    @Override
    public void sendCSVFile(MultipartFile file) {
        var url = theaterBaseUrl + "/upload";
        ServiceCallUtil.sendFile(url, file);
    }

}
