package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Утилитарный класс для выполнения HTTP запросов к API
 */
public class HttpClientUtil {
    private final HttpClient client;

    public HttpClientUtil() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpResponse<String> doGet(String url) {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        return client.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString()).join();
    }

    public HttpResponse<String> doPost(String url, String body) {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString()).join();
    }
}
