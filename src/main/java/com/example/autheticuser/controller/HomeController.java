package com.example.autheticuser.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {

    @GetMapping("/")
    public String redirectToSwaggerUi() {
        // A sintaxe correta para redirecionamento no Spring MVC
        return "redirect:/swagger-ui/index.html";
    }
}
