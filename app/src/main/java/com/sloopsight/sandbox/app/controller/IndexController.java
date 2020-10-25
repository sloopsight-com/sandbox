package com.sloopsight.sandbox.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    @GetMapping("/")
    public String projects() {
        return "redirect:index.html#/login";
    }
}
