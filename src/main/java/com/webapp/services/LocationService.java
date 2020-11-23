package com.webapp.services;

import com.webapp.models.City;

import java.util.List;
import java.util.Map;

public interface LocationService {

    List<City> getAllCities();

    City addCity(City city);

    City updateCity(String id, City city);

    City findById(String id);

    void deleteCity(String id);

//    void addMultipleCities(List<String> cities);

//    Map<String, String> validateBatchExistence(List<String> cities);

}
