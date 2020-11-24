package com.webapp.services;

import com.webapp.models.Movie;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MovieServiceImpl implements MovieService {

    private final String movieBaseUrl = "http://localhost:8082/v1/movies/";

    @Override
    public Movie addMovie(Movie movie) {
        return (Movie) ServiceCallUtil.postPutForEntity(movieBaseUrl, HttpMethod.POST, Movie.class, Movie.class, movie);
    }

    @Override
    public Movie findById(String id) {
        var url = movieBaseUrl + id.trim();
        return (Movie) ServiceCallUtil.getForEntity(url, Movie.class);
    }

    @Override
    public Movie updateMovie(String id, Movie movie) {
        var url = movieBaseUrl + id.trim();
        return (Movie) ServiceCallUtil.postPutForEntity(url, HttpMethod.PUT, Movie.class, Movie.class, movie);
    }

    @Override
    public void deleteMovie(String id) {
        var url = movieBaseUrl + id.trim();
        ServiceCallUtil.delete(url);
    }

    @Override
    public void sendCSVFile(MultipartFile file) {
        var url = movieBaseUrl + "/upload";
        ServiceCallUtil.sendFile(url, file);
    }
}
