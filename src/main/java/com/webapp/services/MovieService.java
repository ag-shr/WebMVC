package com.webapp.services;

import com.webapp.models.Movie;
import org.springframework.web.multipart.MultipartFile;

public interface MovieService {

    Movie addMovie(Movie movie);

    Movie findById(String id);

    Movie updateMovie(String id, Movie movie);

    void deleteMovie(String id);

    void sendCSVFile(MultipartFile file);

}
