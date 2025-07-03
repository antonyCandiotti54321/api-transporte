package com.antonycandiotti.api_transporte.healthCheck;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthCheckController {

   @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        System.out.println("OK Ping Web");
        return ResponseEntity.ok("OK");
    }


}
