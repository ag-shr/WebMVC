package com.webapp.services;

import com.webapp.models.City;
import com.webapp.models.Screen;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ScreenServiceImpl implements ScreenService {

	private final String screenBaseUrl = "http://localhost:8080/v1/screens/";

	@Override
	public Screen addScreen(Screen screen) {
		//POST
		return null;
	}

	@Override
	public void deleteScreen(String screenId) {
		//DELETE
		String url = screenBaseUrl + screenId;
	}

	@Override
	public List<Screen> findScreensPlayingMovie(String theaterId, String movieId) {
		//GET
		String url = screenBaseUrl + "theaterId/" + theaterId + "/" + movieId;
		return null;
	}

	@Override
	public List<Screen> findScreensForATheater(String theaterId) {
		//GET
		String url = screenBaseUrl + "theaterId/" + theaterId;
		return null;
	}

	@Override
	public List<Screen> addScreens(MultipartFile file) {
		//POST
		String url = screenBaseUrl + "upload";
		return null;
	}
}
