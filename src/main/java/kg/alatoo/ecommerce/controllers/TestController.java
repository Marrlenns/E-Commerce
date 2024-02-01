package kg.alatoo.ecommerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @GetMapping("/bye")
    public String bye(){
        return "Good Bye World!";
    }

    @GetMapping("/salam")
    public String salam(){
        return "Salam World!";
    }

    @GetMapping("/adyl")
    public String adyl(){
        return "Adyl World!";
    }

}
