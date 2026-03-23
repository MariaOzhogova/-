package com.project.model.web;

import com.project.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class OpenController {

    @GetMapping("/")
    public String index(ModelMap map) {
        Model model = new Model();
        model.setIntensity0(100);
        model.setPhi(0);
        model.calculate();
        map.addAttribute("model", model);
        return "index";
    }

    @GetMapping("/api/calculate")
    @ResponseBody
    public Map<String, Double> calculateApi(
            @RequestParam double intensity0,
            @RequestParam double phi) {
        Model model = new Model();
        model.setIntensity0(intensity0);
        model.setPhi(phi);
        model.calculate();
        return Map.of("result", model.getResult());
    }
}