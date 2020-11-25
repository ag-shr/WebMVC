package com.webapp.services;

import com.webapp.models.City;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    @Value("${service.location.url}")
    private String locationBaseUrl;

    @Override
    public List<City> getAllCities() {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(locationBaseUrl));
        return (List<City>) response;
    }

    @Override
    public City addCity(City city) {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(locationBaseUrl, HttpMethod.POST, City.class, city));
        return (City) MappingUtilities.retrieveEntity(response, "City");
    }

    @Override
    public City updateCity(String id, City city) {
        String url = locationBaseUrl + id;
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(url, HttpMethod.PUT, City.class, city));
        return (City) MappingUtilities.retrieveEntity(response, "City");
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
        ResponseHandler.handleServiceResponse(ServiceCallUtil.delete(url));
    }

    @Override
    public void sendCSVFile(MultipartFile file) {
        var url = locationBaseUrl + "/upload";
        ResponseHandler.handleServiceResponse(ServiceCallUtil.sendFile(url, file));
    }

}
