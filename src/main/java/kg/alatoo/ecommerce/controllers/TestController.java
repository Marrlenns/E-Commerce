package kg.alatoo.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MessageSource messageSource;
    //Dordoi eto che
    @GetMapping("/hello")
    public String hello(){
        return messageSource.getMessage("common.hello", null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/bye")
    public String bye(@RequestHeader(name = "Accept-Language", required = false) Locale locale){
        return messageSource.getMessage("common.hello", null, LocaleContextHolder.getLocale());
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
