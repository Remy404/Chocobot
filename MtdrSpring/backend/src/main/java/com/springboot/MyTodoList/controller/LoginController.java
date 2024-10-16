package com.springboot.MyTodoList.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

   @GetMapping("/login")
public String login() {
    return "login";  // Nombre del archivo de plantilla del log in
}
}