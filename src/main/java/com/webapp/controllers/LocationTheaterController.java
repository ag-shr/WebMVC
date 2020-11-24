package com.webapp.controllers;

import com.webapp.models.Movie;
import com.webapp.models.Theater;
import com.webapp.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
@CrossOrigin
public class LocationTheaterController {

    private final TheaterService theaterService;

    public LocationTheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<List<Movie>> getFullMoviesInCity(@PathVariable("id") String cityId) {
        return new ResponseEntity<>(theaterService.getFullMoviesInCity(cityId), HttpStatus.OK);
    }

    @GetMapping("{id}/theaters/{movieId}")
    public ResponseEntity<List<Theater>> getTheatersRunningThisMovie(@PathVariable("id") String cityId,
                                                                     @PathVariable("movieId") String movieId) {

        return new ResponseEntity<>(theaterService.getTheatersRunningThisMovie(cityId, movieId), HttpStatus.OK);
    }

}
