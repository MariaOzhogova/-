package com.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Model {
    private double Intensity_0;
    private double Intensity;
    private double Alpha;

    public double calculate_Intensity() {
        Intensity = Intensity_0 * (Math.cos(Alpha)) * (Math.cos(Alpha));
        return Intensity;
    }
}