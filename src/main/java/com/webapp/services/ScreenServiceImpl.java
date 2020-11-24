package com.webapp.services;

import com.webapp.models.Screen;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ScreenServiceImpl implements ScreenService {

	@Value("${service.screen.URL}")
	private String screenBaseUrl;

	@Override
	public Screen addScreen(Screen screen) {
		//POST
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(screenBaseUrl, HttpMethod.POST, Screen.class, screen));
		return (Screen) MappingUtilities.retrieveEntity(response, "Screen");
	}

	@Override
	public void deleteScreen(String screenId) {
		//DELETE
		String url = screenBaseUrl + screenId;
		ResponseHandler.handleServiceResponse(ServiceCallUtil.delete(url));
	}

	@Override
	public List<Screen> findScreensPlayingMovie(String theaterId, String movieId) {
		String url = screenBaseUrl + "theater/" + theaterId + "/" + movieId;
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
		return (List<Screen>) response;
	}

	@Override
	public List<Screen> findScreensForATheater(String theaterId) {
		String url = screenBaseUrl + "theater/" + theaterId;
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
		return (List<Screen>) response;
	}

	@Override
	public List<Screen> addScreens(MultipartFile file) {
		String url = screenBaseUrl + "upload";
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.sendFile(url, file));
		return (List<Screen>) response;
	}
}
