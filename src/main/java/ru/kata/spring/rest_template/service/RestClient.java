package ru.kata.spring.rest_template.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.kata.spring.rest_template.model.User;

public class RestClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://94.198.50.185:7081/api/users";

        ResponseEntity<String> getResponse = restTemplate.getForEntity(url, String.class);
        System.out.println("Users list: " + getResponse.getBody());

        String setCookie = getResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("Set-Cookie: " + setCookie);

        String sessionId = setCookie.split(";")[0];
        System.out.println("Session ID: " + sessionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionId);

        User newUser = new User(3L, "James", "Brown", (byte) 30);
        HttpEntity<User> postRequest = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> postResponse = restTemplate.postForEntity(url, postRequest, String.class);
        String part1 = postResponse.getBody();
        System.out.println("First part: " + part1);

        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 30);
        HttpEntity<User> putRequest = new HttpEntity<>(updatedUser, headers);

        ResponseEntity<String> putResponse = restTemplate.exchange(url, HttpMethod.PUT, putRequest, String.class);
        String part2 = putResponse.getBody();
        System.out.println("Second part: " + part2);

        String deleteUrl = url + "/3";
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);

        ResponseEntity<String> deleteResponse = restTemplate.exchange(deleteUrl, HttpMethod.DELETE,
                deleteRequest, String.class);
        String part3 = deleteResponse.getBody();
        System.out.println("Third part: " + part3);

        String resultCode = part1 + part2 + part3;
        System.out.println("==========================================");
        System.out.println("RESULT CODE: " + resultCode);
        System.out.println("Code length:" + resultCode.length() + " symbols");
        System.out.println("==========================================");
    }
}
