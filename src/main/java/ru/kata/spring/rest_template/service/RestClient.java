package ru.kata.spring.rest_template.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.kata.spring.rest_template.model.User;

public class RestClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://94.198.50.185:7081/api/users";

        ResponseEntity<String> getResponse = restTemplate.getForEntity(url, String.class);
        System.out.println("===INITIAL USERS LIST===");
        System.out.println(getResponse.getBody());
        System.out.println();

        String setCookie = getResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("Set-Cookie: " + setCookie);
        String sessionId = setCookie.split(";")[0];
        System.out.println("Session ID: " + sessionId);
        System.out.println();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionId);

        User newUser = new User(3L, "James", "Brown", (byte) 30);
        HttpEntity<User> postRequest = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> postResponse = restTemplate.postForEntity(url, postRequest, String.class);
        String part1 = postResponse.getBody();
        System.out.println("===AFTER POST STATEMEN (James Brown)===");
        System.out.println("First part: " + part1);

        ResponseEntity<String> afterPostResponse = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        System.out.println("Users list after POST:");
        System.out.println(afterPostResponse.getBody());
        System.out.println();

        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 30);
        HttpEntity<User> putRequest = new HttpEntity<>(updatedUser, headers);

        ResponseEntity<String> putResponse = restTemplate.exchange(
                url, HttpMethod.PUT, putRequest, String.class);
        String part2 = putResponse.getBody();
        System.out.println("===AFTER PUT STATEMEN (Thomas Shelby)===");
        System.out.println("Second part: " + part2);
        ResponseEntity<String> afterPutResponse = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        System.out.println("Users list after PUT:");
        System.out.println(afterPutResponse.getBody());
        System.out.println();

        String deleteUrl = url + "/3";
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);

        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                deleteUrl, HttpMethod.DELETE, deleteRequest, String.class);
        String part3 = deleteResponse.getBody();
        System.out.println("===AFTER DELETE STATEMEN (id=3 user deletion)===");
        System.out.println("Third part: " + part3);
        ResponseEntity<String> afterDeleteResponse = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        System.out.println("Final users list:");
        System.out.println(afterDeleteResponse.getBody());
        System.out.println();

        String resultCode = part1 + part2 + part3;
        System.out.println("==========================================");
        System.out.println("RESULT CODE: " + resultCode);
        System.out.println("Code length:" + resultCode.length() + " symbols");
        System.out.println("==========================================");
    }
}
