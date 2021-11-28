package com.notayessir.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ProviderController {


    @RequestMapping("hello")
    public String hello(){
        return "hello, this is provider.";
    }

}
