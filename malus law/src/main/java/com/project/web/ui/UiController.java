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
    public String title() {
        return "title";
    }

    @GetMapping("/malus-law")
    public String malusLaw(ModelMap map) {
        log.info("GET /malus-law - opening Malus law laboratory page");
        Model model = Model.builder().intensity0(100).phi(0).build();
        model.calculate();
        map.addAttribute("model", model);
        log.debug("Initial values: I0={}, phi={}, result={}", model.getIntensity0(), model.getPhi(), model.getResult());
        return "malus-law";
    }
}
