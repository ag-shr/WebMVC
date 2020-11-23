package com.webapp.controllers;

import com.webapp.models.City;
import com.webapp.services.LocationService;
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

@RestController
@RequestMapping("cities")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return new ResponseEntity<>(locationService.getAllCities(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<City> addCity(@Valid @RequestBody City city) {
        return new ResponseEntity<>(locationService.addCity(city), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") String id, @Valid @RequestBody City city) {
        return new ResponseEntity<>(locationService.updateCity(id, city), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<City> findCityById(@PathVariable("id") String id) {
        return new ResponseEntity<>(locationService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable("id") String id) {
        locationService.deleteCity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addCitiesViaFile(@RequestParam("file") MultipartFile file) throws IOException {
//        var cities = CSVConverter.csvToCities(file.getInputStream());
//        locationService.addMultipleCities(cities);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @PostMapping("/batch")
//    public ResponseEntity<Void> addMultipleCities(@RequestBody List<@NotNull String> cities) {
//        locationService.addMultipleCities(cities);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

//    @PostMapping("/existence")
//    public ResponseEntity<Map<String, String>> checkBatchExistence(@RequestBody List<@NotNull String> cityIds) {
//        return new ResponseEntity<>(locationService.validateBatchExistence(cityIds), HttpStatus.OK);
//    }

}
