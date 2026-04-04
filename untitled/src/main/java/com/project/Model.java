package com.project;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@Slf4j
public class Model {
    private double intensity0;
    private double phi;
    private double result;

    public double calculate() {
        log.debug("Расчёт: I₀={}, φ={}°", intensity0, phi);
        double rad = Math.toRadians(phi);
        result = 0.5 * intensity0 * Math.pow(Math.cos(rad), 2);
        log.debug("Результат: I_A={}", result);
        return result;
    }
}