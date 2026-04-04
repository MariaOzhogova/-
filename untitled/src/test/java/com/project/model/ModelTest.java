package com.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void calculate_phi0_returnsHalfIntensity() {
        // При φ=0°: cos(0)=1, результат = 0.5 * I₀
        Model model = new Model();
        model.setIntensity0(100);
        model.setPhi(0);

        double result = model.calculate();

        assertEquals(50.0, result, 0.0001);
    }

    @Test
    void calculate_phi90_returnsZero() {
        // При φ=90°: cos(90°)=0, результат = 0
        Model model = new Model();
        model.setIntensity0(100);
        model.setPhi(90);

        double result = model.calculate();

        assertEquals(0.0, result, 0.0001);
    }

    @Test
    void calculate_phi45_returnsQuarterIntensity() {
        // При φ=45°: cos²(45°)=0.5, результат = 0.25 * I₀
        Model model = new Model();
        model.setIntensity0(100);
        model.setPhi(45);

        double result = model.calculate();

        assertEquals(25.0, result, 0.0001);
    }

    @Test
    void calculate_updatesResultField() {
        // Проверяем, что getResult() после вызова возвращает то же, что calculate()
        Model model = new Model();
        model.setIntensity0(80);
        model.setPhi(60);

        double returned = model.calculate();

        assertEquals(returned, model.getResult(), 0.0001);
    }

    @Test
    void calculate_zeroIntensity_returnsZero() {
        Model model = new Model();
        model.setIntensity0(0);
        model.setPhi(45);

        double result = model.calculate();

        assertEquals(0.0, result, 0.0001);
    }
}