package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.models.Theater;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
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
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
        return (Theater) MappingUtilities.retrieveEntity(response, "Theater");
    }

    @Override
    public Theater addTheater(Theater theater) {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(theaterBaseUrl, HttpMethod.POST, Theater.class, theater));
        return (Theater) MappingUtilities.retrieveEntity(response, "Theater");
    }

    @Override
    public void removeTheater(String theaterId) {
        var url = theaterBaseUrl + theaterId;
        ResponseHandler.handleServiceResponse(ServiceCallUtil.delete(url));
    }

    @Override
    public Theater updateTheater(String id, Theater theater) {
        var url = theaterBaseUrl + id.trim();
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(url, HttpMethod.PUT, Theater.class, theater));
        return (Theater) MappingUtilities.retrieveEntity(response, "Theater");
    }

    @Override
    public Theater addMovieInTheater(String theaterId, String movieId) {
        var url = theaterBaseUrl + theaterId + "/movies/" + movieId;
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(url, HttpMethod.POST, Theater.class, NO_BODY));
        return (Theater) MappingUtilities.retrieveEntity(response, "Theater");
    }

    @Override
    public void removeMovieFromTheater(String theaterId, String movieId) {
        var url = theaterBaseUrl + theaterId + "/movies/" + movieId;
        ResponseHandler.handleServiceResponse(ServiceCallUtil.delete(url));
    }

    @Override
    public List<Movie> getFullMoviesInCity(String cityId) {
        var url = locationBaseUrl + cityId + "/movies/details";
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
        return (List<Movie>) response;
    }

    @Override
    public List<Theater> getTheatersRunningThisMovie(String cityId, String movieId) {
        var url = locationBaseUrl + cityId + "/theaters/" + movieId;
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
        return (List<Theater>) response;
    }

    @Override
    public void sendCSVFile(MultipartFile file) {
        var url = theaterBaseUrl + "/upload";
        ResponseHandler.handleServiceResponse(ServiceCallUtil.sendFile(url, file));
    }

}
