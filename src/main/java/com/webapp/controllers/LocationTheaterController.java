package com.webapp.controllers;

import com.webapp.models.Movie;
import com.webapp.models.Theater;
import com.webapp.models.Theater.ShortMovie;
import com.webapp.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("cities")
@CrossOrigin
public class LocationTheaterController {

    private final TheaterService theaterService;

    public LocationTheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("{id}/theaters")
    public ResponseEntity<List<Theater>> getTheatersInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getTheatersInCity(cityId), HttpStatus.OK);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<Set<ShortMovie>> getMoviesInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getMoviesInCity(cityId), HttpStatus.OK);
    }

    @GetMapping("{id}/movies/details")
    public ResponseEntity<List<Movie>> getFullMoviesInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getFullMoviesInCity(cityId), HttpStatus.OK);
    }

    @PostMapping("{id}/theaters")
    public ResponseEntity<Void> addMultipleTheatersInCity(@PathVariable("id") String cityId,
                                                          @RequestBody List<@NotNull @Valid Theater> theaters) {

        theaterService.addMultipleTheaters(theaters, cityId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}/theaters/{movieId}")
    public ResponseEntity<List<Theater>> getTheatersRunningThisMovie(@PathVariable("id") String cityId,
                                                                     @PathVariable("movieId") String movieId) {

        return new ResponseEntity<>(theaterService.getTheatersRunningThisMovie(cityId, movieId), HttpStatus.OK);
    }

    @DeleteMapping("{id}/theaters")
    public ResponseEntity<Void> removeAllTheatersFromCity(@PathVariable("id") String cityId) {
        theaterService.removeTheatersFromCity(cityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}