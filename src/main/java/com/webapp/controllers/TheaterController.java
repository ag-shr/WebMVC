package com.webapp.controllers;

import com.webapp.models.Theater;
import com.webapp.models.Theater.ShortMovie;
import com.webapp.services.TheaterService;
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
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("theaters")
public class TheaterController {

    private final TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return new ResponseEntity<>(theaterService.getAllTheaters(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Theater> addTheater(@Valid @RequestBody Theater theater) {
        return new ResponseEntity<>(theaterService.addTheater(theater), HttpStatus.CREATED);
    }

    @PostMapping("batch")
    public ResponseEntity<Void> addMultipleTheaters(@RequestBody List<@NotNull @Valid Theater> theaters) {
        theaterService.addMultipleTheaters(theaters);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Theater> findById(@PathVariable("id") String id) {
        return new ResponseEntity<>(theaterService.findTheaterById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable("id") String id, @Valid @RequestBody Theater theater) {
        return new ResponseEntity<>(theaterService.updateTheater(id, theater), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeTheater(@PathVariable("id") String id) {
        theaterService.removeTheater(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> removeTheseMoviesFromTheseTheaters(@RequestBody Map<String, Set<String>> map) {
        theaterService.removeTheseMoviesFromTheseTheaters(map);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/{movieId}")
    public ResponseEntity<Boolean> validateTheaterAndMovie(@PathVariable("id") String theaterId,
                                                           @PathVariable("movieId") String movieId) {

        return new ResponseEntity<>(theaterService.validateTheaterAndMovie(theaterId, movieId), HttpStatus.OK);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<List<ShortMovie>> getMoviesInATheater(@PathVariable("id") String id) {
        return new ResponseEntity<>(theaterService.getMovies(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/movies/{movieId}")
    public ResponseEntity<Theater> addMovieToTheater(@PathVariable("id") @NotNull String id,
                                                     @PathVariable("movieId") @NotNull String movieId) {

        return new ResponseEntity<>(theaterService.addMovieInTheater(id, movieId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/movies/{movieId}")
    public ResponseEntity<Void> removeMovieFromTheater(@PathVariable("id") String theaterId,
                                                       @PathVariable("movieId") String movieId) {

        theaterService.removeMovieFromTheater(theaterId, movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void>  addTheatersViaFile(@RequestParam("file") MultipartFile file) throws IOException {
//        var theaters = csvConverter.csvToTheaters(file.getInputStream());
//        theaterService.addMultipleTheaters(theaters);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

