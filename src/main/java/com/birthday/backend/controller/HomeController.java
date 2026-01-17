package com.birthday.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "online");
        response.put("message", "scribl backend is up and running");
        response.put("info", "use the frontend to interact with the cards");
        return response;
    }
}
