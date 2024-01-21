package org.HttpCommonUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;


public class httpUtilities {
    public static HttpClient buildClient(){
        return HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }
    public static HttpRequest buildHttpGetRequest(String url) throws URISyntaxException {
        return HttpRequest
                .newBuilder(new URI(url))
                .method("GET",HttpRequest.BodyPublishers.noBody())
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .build();
    }

}
