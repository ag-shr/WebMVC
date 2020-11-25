package com.webapp.controllers;

import com.webapp.models.Movie;
import com.webapp.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("movies")
@CrossOrigin
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Movie> findMovie(@PathVariable("id") String id) {
        return new ResponseEntity<>(movieService.findById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") String id, @Valid @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.updateMovie(id, movie), HttpStatus.OK);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addMoviesViaFile(@RequestParam("file") MultipartFile file) {
        movieService.sendCSVFile(file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
