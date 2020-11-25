package com.webapp.utilities;

import com.webapp.RequestResponseClasses.MicroserviceResponse;
import com.webapp.jwt.JwtConstants;
import com.webapp.services.ClientAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
@Component
public class ServiceCallUtil {

	private static ClientAccessTokenService clientAccessTokenService;

	@Autowired
	private ClientAccessTokenService clientAccessTokenServiceTemp;

	@PostConstruct
	private void initStaticService () {
		clientAccessTokenService = this.clientAccessTokenServiceTemp;
	}

	public static MicroserviceResponse get(String url) {
		return getWebclient()
		  .get()
		  .uri(URI.create(url))
		  .retrieve()
		  .bodyToMono(MicroserviceResponse.class)
		  .block();

	}

	public static MicroserviceResponse postPutForEntity(String url, HttpMethod method, Class<?> requestClass, Object data) {
		return getWebclient()
		  .method(method)
		  .uri(URI.create(url))
		  .body(Mono.just(data), requestClass)
		  .retrieve()
		  .bodyToMono(MicroserviceResponse.class)
		  .block();
	}

	public static MicroserviceResponse putList(String url, Class<?> claas, List<?> list) {
		return getWebclient()
		  .put()
		  .uri(URI.create(url))
		  .body(Mono.just(list), claas)
		  .retrieve()
		  .bodyToMono(MicroserviceResponse.class)
		  .block();
	}

	public static MicroserviceResponse delete(String url) {
		return getWebclient()
		  .delete()
		  .uri(URI.create(url))
		  .retrieve()
		  .bodyToMono(MicroserviceResponse.class)
		  .block();
	}

	public static MicroserviceResponse sendFile(String url, MultipartFile file) {
		return getWebclient()
		  .post()
		  .uri(URI.create(url))
		  .contentType(MediaType.MULTIPART_FORM_DATA)
		  .body(BodyInserters.fromMultipartData(fromFile(file)))
		  .retrieve()
		  .bodyToMono(MicroserviceResponse.class)
		  .block();
	}

	private static MultiValueMap<String, HttpEntity<?>> fromFile(MultipartFile file) {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", file.getResource());
		return builder.build();
	}

	private static WebClient getWebclient() {
		return WebClient.builder().defaultHeaders(httpHeaders -> {
			httpHeaders.add(JwtConstants.AUTHORIZATION_HEADER,
			  JwtConstants.BEARER_TOKEN_TYPE + clientAccessTokenService.getAccessToken());
			httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		}).build();
	}
}
