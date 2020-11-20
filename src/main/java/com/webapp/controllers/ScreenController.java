package com.webapp.controllers;

import com.webapp.models.Screen;
import com.webapp.services.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/v1/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<Screen>> getScreensForATheater(@PathVariable("theaterId") String theaterId) {
        return new ResponseEntity<>(screenService.findScreensForATheater(theaterId),
                HttpStatus.OK);
    }

    @GetMapping("/theater/{theaterId}/{movieId}")
    public ResponseEntity<List<Screen>> getScreensPlayingMovie(@PathVariable("theaterId") String theaterId,
                                                               @PathVariable("movieId") String movieId) {
        return new ResponseEntity<>(screenService.findScreensPlayingMovie(theaterId, movieId),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Screen> addScreenToTheater(@Valid @RequestBody Screen screen) {
        return new ResponseEntity<>(screenService.addScreen(screen), HttpStatus.OK);
    }

    @DeleteMapping("/{screenId}")
    public ResponseEntity<Void> removeScreen(@PathVariable("screenId") String screenId) {
        screenService.deleteScreen(screenId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Screen>> addMultipleScreens(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        try {
            return new ResponseEntity<>(screenService.addScreens(file), HttpStatus.OK);
        } catch (Exception e) {
            throw new FileNotFoundException("Please send a CSV file to add screens.");
        }
    }
}
