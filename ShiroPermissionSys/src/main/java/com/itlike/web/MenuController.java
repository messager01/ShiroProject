package com.itlike.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuController {
    @RequestMapping("/menu")
    public String employee(){
        return "menu";
    }
}
