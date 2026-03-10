package com.project.model.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@Controller
public class OpenController {

    @GetMapping("/")
    public String index() {
        // _csrf автоматически доступен в Thymeleaf через Spring Security
        return "index";
    }

    @PostMapping("/api/save-lab")
    @ResponseBody
    public String saveLab(@RequestBody Map<String, Object> data) {
        System.out.println("Данные получены: " + data);
        return "OK";
    }
}