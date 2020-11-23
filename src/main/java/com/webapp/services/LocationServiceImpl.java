package com.webapp.services;

import com.webapp.models.City;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    private final String locationBaseUrl = "http://localhost:8080/v1/cities/";

    @Override
    public List<City> getAllCities() {
        return (List<City>) ServiceCallUtil.getForList(locationBaseUrl, City.class);
    }

    @Override
    public City addCity(City city) {
        return (City) ServiceCallUtil.postPutForEntity(locationBaseUrl, HttpMethod.POST, City.class, City.class, city);
    }

    @Override
    public City updateCity(String id, City city) {
        String url = locationBaseUrl + id;
        return (City) ServiceCallUtil.postPutForEntity(url, HttpMethod.PUT, City.class, City.class, city);
    }

    @Override
    public City findById(String id) {
        String url = locationBaseUrl + id;
        return (City) ServiceCallUtil.getForEntity(url, City.class);
    }

    @Override
    public void deleteCity(String id) {
        String url = locationBaseUrl + id;
        ServiceCallUtil.delete(url);
    }

}
