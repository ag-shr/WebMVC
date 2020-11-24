package com.webapp.services;

import com.webapp.models.City;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    private final String locationBaseUrl = "http://localhost:8080/v1/cities/";

    @Override
    public List<City> getAllCities() {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(locationBaseUrl));
        return (List<City>) response;
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
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
        return (City) MappingUtilities.retrieveEntity(response, "City");
    }

    @Override
    public void deleteCity(String id) {
        String url = locationBaseUrl + id;
        ServiceCallUtil.delete(url);
    }

    @Override
    public void sendCSVFile(MultipartFile file) {
        var url = locationBaseUrl + "/upload";
        ServiceCallUtil.sendFile(url, file);
    }

}
