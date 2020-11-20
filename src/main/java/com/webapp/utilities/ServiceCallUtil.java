package com.webapp.utilities;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
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

    public static Object postPutForEntity(String url, HttpMethod method, Class<?> claas, Object data) {
        return WebClient.create()
          .method(method)
          .uri(URI.create(url))
          .body(Mono.just(data), claas)
          .retrieve()
          .bodyToMono(claas)
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

    public static void delete(String url) {
        WebClient.create()
          .delete()
          .uri(URI.create(url))
          .retrieve()
          .bodyToMono(Void.class);
    }

}
