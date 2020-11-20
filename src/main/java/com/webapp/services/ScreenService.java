package com.webapp.services;

import com.webapp.models.Screen;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScreenService {

	Screen addScreen(Screen screen);

	void deleteScreen(String screenId);

	List<Screen> findScreensPlayingMovie(String theaterId, String movieId);

	List<Screen> findScreensForATheater(String theaterId);

	List<Screen> addScreens(MultipartFile file);
}
