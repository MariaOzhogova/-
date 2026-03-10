package com.project.model;

public class Model {
    public double Intensity_0;
    public double Intensity;
    public double Alpha;

    public double calculate_Intensity() {
        Intensity = Intensity_0 * (Math.cos(Alpha)) * (Math.cos(Alpha));
        return Intensity;
    }

    public void setAlpha(double alpha) {
        Alpha = alpha;
    }

    public void setIntensity_0(double intensity_0) {
        Intensity_0 = intensity_0;
    }

    public void setIntensity(double intensity) {
        Intensity = intensity;
    }

    public double getIntensity() {
        return Intensity;
    }

}