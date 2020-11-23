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
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("movies")
@CrossOrigin
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.OK);
    }

    @GetMapping("latest")
    public ResponseEntity<List<Movie>> getLatestMovies() {
        return new ResponseEntity<>(movieService.getLatestMovies(), HttpStatus.OK);
    }

    @PostMapping("findByIds")
    public ResponseEntity<Set<Movie>> getMoviesByIds(@RequestBody Set<String> movieIds) {
        return new ResponseEntity<>(movieService.getMoviesByIds(movieIds), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.CREATED);
    }

    @PostMapping("batch")
    public ResponseEntity<Void> addMovies(@RequestBody List<@NotNull @Valid Movie> movies) {
        movieService.addMultipleMovies(movies);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Movie> findMovie(@PathVariable("id") String id) {
        return new ResponseEntity<>(movieService.findById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") String id, @Valid @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.updateMovie(id, movie), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable("id") String id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addMoviesViaFile(@RequestParam("file") MultipartFile file) throws IOException {
//        var movies = CSVConverter.csvToMovies(file.getInputStream());
//        movieService.addMultipleMovies(movies);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
