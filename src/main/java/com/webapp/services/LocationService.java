package com.webapp.services;

import com.webapp.models.City;

import java.util.List;

public interface LocationService {

    List<City> getAllCities();

    City addCity(City city);

    City updateCity(String id, City city);

    City findById(String id);

    void deleteCity(String id);

}
