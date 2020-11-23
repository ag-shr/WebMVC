package com.webapp.services;

import com.webapp.models.City;
import com.webapp.models.Screen;
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
		return (Screen) ServiceCallUtil.postPutForEntity(screenBaseUrl,HttpMethod.POST,Screen.class,Screen.class,screen);
	}

	@Override
	public void deleteScreen(String screenId) {
		//DELETE
		String url = screenBaseUrl + screenId;
		ServiceCallUtil.delete(url);
	}

	@Override
	public List<Screen> findScreensPlayingMovie(String theaterId, String movieId) {
		String url = screenBaseUrl + "theater/" + theaterId + "/" + movieId;
		return (List<Screen>) ServiceCallUtil.getForList(url,Screen.class);
	}

	@Override
	public List<Screen> findScreensForATheater(String theaterId) {
		String url = screenBaseUrl + "theater/" + theaterId;
		return (List<Screen>) ServiceCallUtil.getForList(url,Screen.class);
	}

	@Override
	public List<Screen> addScreens(MultipartFile file) {
		String url = screenBaseUrl + "upload";
		return (List<Screen>)ServiceCallUtil.sendFile(url,file);
	}
}
