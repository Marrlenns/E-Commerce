package kg.alatoo.ecommerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    //Dordoi eto che
    @GetMapping("/hello")
    public String hello(){
        return "Hello Chushpan!";
    }

    @GetMapping("/bye")
    public String bye(){
        return "Good Bye World!";
    }

    @GetMapping("/salam")
    public String salam(){
        return "Salam World!";
    }

    @GetMapping("/salam2")
    public String salam2(){
        return "Salam World!";
    }
}
