package com.project.web.ui;

import com.project.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UiController {

    @GetMapping("/")
    public String index(ModelMap map) {
        log.info("GET / — открытие главной страницы");
        Model model = Model.builder().intensity0(100).phi(0).build();
        model.calculate();
        map.addAttribute("model", model);
        log.debug("Начальные значения: I₀={}, φ={}, результат={}", model.getIntensity0(), model.getPhi(), model.getResult());
        return "index";
    }
}
