package com.APIZero.Zero.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping
    public Map<String, String> helloWorld(){
        return Map.of("message", "Hello World!");
    }

}
