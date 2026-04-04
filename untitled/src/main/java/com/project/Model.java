package com.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Model {
    private double intensity0;
    private double phi;
    private double result;

    public double calculate() {
        double rad = Math.toRadians(phi);
        result = 0.5 * intensity0 * Math.pow(Math.cos(rad), 2);
        return result;
    }
}