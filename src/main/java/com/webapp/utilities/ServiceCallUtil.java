package com.webapp.utilities;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

public class ServiceCallUtil {

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

}
