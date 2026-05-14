package com.gialamclinic.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public String test(){

        return "JWT working";

    }

}