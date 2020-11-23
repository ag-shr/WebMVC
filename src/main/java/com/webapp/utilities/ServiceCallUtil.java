package com.webapp.utilities;

import com.webapp.jwt.JwtConstants;
import com.webapp.services.ClientAccessTokenService;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

public class ServiceCallUtil {

	private ClientAccessTokenService clientAccessTokenService;

	public static Object getForEntity(String url, Class<?> claas) {
		return WebClient.create()
		  .get()
		  .uri(URI.create(url))
		  .retrieve()
		  .bodyToMono(claas)
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

	public static void postList(String url, Class<?> claas, List<?> list) {
		WebClient.create()
		  .post()
		  .uri(URI.create(url))
		  .body(Mono.just(list), claas)
		  .retrieve()
		  .bodyToMono(Void.class);
	}

    public static Object postList(RestTemplate restTemplate, String url, HttpMethod method, Class<?> claas, List<?> list) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(list, headers);
        return restTemplate.exchange(url, method, entity, claas).getBody();
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
		return WebClient.builder().baseUrl(baseUrl).defaultHeaders(httpHeaders -> {
			httpHeaders.add(JwtConstants.AUTHORIZATION_HEADER,
			  JwtConstants.BEARER_TOKEN_TYPE + clientAccessTokenService.getAccessToken());
			httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		}).build();
	}
}
