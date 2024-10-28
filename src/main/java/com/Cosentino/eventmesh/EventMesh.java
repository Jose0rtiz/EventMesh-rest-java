package com.cosentino.eventmesh;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EventMesh {

    private static final String eventMeshUrl = "...";
    private static final String queueName = "...";
    private static final String tokenEndpoint = "...";
    private static final String clientId = "...";
    private static final String clientSecret = "...";

    @SuppressWarnings("unchecked")
    public static String getToken() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        Map<Object, Object> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        formParams.put("response_type", "token");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(buildFormDataFromMap(formParams)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = mapper.readValue(response.body(), Map.class);
        return (String) responseBody.get("access_token");
    }

    public static void sendMessage(String message) throws Exception {
        String token = getToken();
        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("message", message);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(messageBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(eventMeshUrl + "/messagingrest/v1/queues/" + queueName + "/messages"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("x-qos", "1")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Mensaje enviado: " + response.statusCode());
    }

    private static String buildFormDataFromMap(Map<Object, Object> data) {
        StringBuilder formBody = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (formBody.length() > 0) {
                formBody.append("&");
            }
            formBody.append(URI.create(entry.getKey().toString())).append("=")
                    .append(URI.create(entry.getValue().toString()));
        }
        return formBody.toString();
    }

    public static void receiveMessage() throws Exception {
        String token = getToken();
        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("message", "");
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(messageBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(eventMeshUrl + "/messagingrest/v1/queues/" + queueName + "/messages/consumption"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("x-qos", "1")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Mensaje recivido: " + response.body());
    }

    public static void main(String[] args) throws Exception {
        sendMessage("Hola, Mesh!");
        receiveMessage();
    }
}

