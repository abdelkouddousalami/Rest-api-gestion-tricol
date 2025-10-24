package com.youcode.controllers;

import com.youcode.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping
    public ResponseEntity<Map<String, String>> sayHello() {
        String message = helloService.getGreeting();
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("success", "true");

        return ResponseEntity.ok(response);
    }
}
