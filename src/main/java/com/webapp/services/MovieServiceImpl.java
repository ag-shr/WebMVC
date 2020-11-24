package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MovieServiceImpl implements MovieService {

    private final String movieBaseUrl = "http://localhost:8082/v1/movies/";

    @Override
    public Movie addMovie(Movie movie) {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(movieBaseUrl, HttpMethod.POST, Movie.class, movie));
        return (Movie) MappingUtilities.retrieveEntity(response, "Movie");
    }

    @Override
    public Movie findById(String id) {
        var url = movieBaseUrl + id.trim();
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
        return (Movie) MappingUtilities.retrieveEntity(response, "Movie");
    }

    @Override
    public Movie updateMovie(String id, Movie movie) {
        var url = movieBaseUrl + id.trim();
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(url, HttpMethod.PUT, Movie.class, movie));
        return (Movie) MappingUtilities.retrieveEntity(response, "Movie");
    }

    @Override
    public void deleteMovie(String id) {
        var url = movieBaseUrl + id.trim();
        ResponseHandler.handleServiceResponse(ServiceCallUtil.delete(url));
    }

    @Override
    public void sendCSVFile(MultipartFile file) {
        var url = movieBaseUrl + "/upload";
        ResponseHandler.handleServiceResponse(ServiceCallUtil.sendFile(url, file));
    }
}
