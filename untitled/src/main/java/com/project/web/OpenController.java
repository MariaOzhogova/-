package com.project.web;

import com.project.Model;
import com.project.model.ErrorRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OpenController {

    private final ErrorRate errorRate;

    @GetMapping("/")
    public String index(ModelMap map) {
        log.info("GET / — открытие главной страницы");
        Model model = Model.builder().intensity0(100).phi(0).build();
        model.calculate();
        map.addAttribute("model", model);
        log.debug("Начальные значения: I₀={}, φ={}, результат={}", model.getIntensity0(), model.getPhi(), model.getResult());
        return "index";
    }

    @GetMapping("/api/calculate")
    @ResponseBody
    public Map<String, Double> calculateApi(
            @RequestParam double intensity0,
            @RequestParam double phi) {
        log.info("GET /api/calculate — intensity0={}, phi={}", intensity0, phi);
        Model model = Model.builder().intensity0(intensity0).phi(phi).build();
        double trueResult = model.calculate();
        double measuredResult = errorRate.applyNoise(trueResult);
        if (errorRate.isActive()) {
            errorRate.getMeasurements().add(measuredResult);
        }
        log.debug("Результат расчёта: I_A={}", measuredResult);
        return Map.of("result", measuredResult);
    }

    @GetMapping("/api/error")
    @ResponseBody
    public Map<String, Object> getError() {
        double absError = errorRate.calculateAbsoluteError();
        log.info("GET /api/error — погрешность={}, измерений={}", absError, errorRate.getMeasurements().size());
        return Map.of(
                "absoluteError", absError,
                "measurementsCount", errorRate.getMeasurements().size(),
                "noiseActive", errorRate.isActive()
        );
    }

    @PostMapping("/api/error/toggle")
    @ResponseBody
    public Map<String, Boolean> toggleNoise() {
        errorRate.setActive(!errorRate.isActive());
        log.info("Шум {}", errorRate.isActive() ? "включён" : "выключен");
        return Map.of("noiseActive", errorRate.isActive());
    }

    @PostMapping("/api/error/reset")
    @ResponseBody
    public Map<String, String> resetMeasurements() {
        errorRate.resetMeasurements();
        return Map.of("status", "ok");
    }
}