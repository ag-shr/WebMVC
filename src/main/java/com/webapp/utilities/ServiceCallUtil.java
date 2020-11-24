package com.webapp.utilities;

import com.webapp.RequestResponseClasses.MicroserviceResponse;
import com.webapp.jwt.JwtConstants;
import com.webapp.services.ClientAccessTokenService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

public class ServiceCallUtil {

	private ClientAccessTokenService clientAccessTokenService;

	public static MicroserviceResponse get(String url) {
		return WebClient.create()
		  .get()
		  .uri(URI.create(url))
		  .retrieve()
		  .bodyToMono(MicroserviceResponse.class)
		  .block();
	}

	public static List<?> getForList(String url, Class<?> claas) {
		return WebClient.create()
		  .get()
		  .uri(URI.create(url))
		  .retrieve()
		  .bodyToFlux(claas)
		  .collectList()
		  .block();
	}

	public static Object postPutForEntity(String url, HttpMethod method, Class<?> requestClass, Class<?> responseClass, Object data) {
		return WebClient.create()
		  .method(method)
		  .uri(URI.create(url))
		  .body(Mono.just(data), requestClass)
		  .retrieve()
		  .bodyToMono(responseClass)
		  .block();
	}

	public static Boolean putList(String url, Class<?> claas, List<?> list) {
		return WebClient.create()
		  .put()
		  .uri(URI.create(url))
		  .body(Mono.just(list), claas)
		  .retrieve()
		  .bodyToMono(Boolean.class)
		  .block();
	}

	public static List<?> sendFile(String url, MultipartFile file) {
		return WebClient.create()
		  .post()
		  .uri(URI.create(url))
		  .contentType(MediaType.MULTIPART_FORM_DATA)
		  .body(BodyInserters.fromMultipartData(fromFile(file)))
		  .retrieve()
		  .bodyToMono(List.class)
		  .block();
	}

	private static MultiValueMap<String, HttpEntity<?>> fromFile(MultipartFile file) {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", file.getResource());
		return builder.build();
	}

	public static void delete(String url) {
		WebClient.create()
		  .delete()
		  .uri(URI.create(url))
		  .retrieve()
		  .bodyToMono(Void.class)
		  .subscribe();
	}

	private WebClient getWebclient(String baseUrl) {
		return WebClient.builder().defaultHeaders(httpHeaders -> {
			httpHeaders.add(JwtConstants.AUTHORIZATION_HEADER,
			  JwtConstants.BEARER_TOKEN_TYPE + clientAccessTokenService.getAccessToken());
			httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		}).build();
	}
}
