package com.webapp.controllers;

import com.webapp.models.City;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;


@Controller
@RequestMapping("v1")
public class MainController {

    @GetMapping("health")
    public ResponseEntity<Boolean> healthCheck() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

//    @GetMapping("cities")
//    public ResponseEntity<List<City>> getAllCities() {
//        return new ResponseEntity<>(
//          WebClient.create()
//            .get()
//            .uri(URI.create("http://localhost:8080/v1/cities"))
//            .retrieve()
//            .bodyToFlux(City.class)
//            .collectList()
//            .block(),
//          HttpStatus.OK
//        );
//    }

}
