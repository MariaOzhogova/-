package com.project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Getter
@Setter
@Slf4j
public class ErrorRate {
    private boolean active = false;
    private List<Double> measurements = new ArrayList<>();
    private Double confidenceLevel = 0.95;
    private Double studentCoefficient = 2.0;
    private Double systematicError = 0.5;

    private final Random random = new Random();

    public double applyNoise(double trueValue) {
        if (!active || trueValue < 0) return trueValue;

        double randomComponent = -1.0 + (2.0 * random.nextDouble());
        double measuredValue = trueValue + systematicError + randomComponent;
        double result = Math.round(measuredValue * 100.0) / 100.0;
        log.debug("Применён шум: истинное={}, измеренное={}", trueValue, result);
        return result;
    }

    public double calculateAbsoluteError() {
        if (measurements.isEmpty()) return systematicError;

        double sum = 0;
        for (double m : measurements) sum += m;
        double average = sum / measurements.size();

        double varianceSum = 0;
        for (double m : measurements) {
            varianceSum += Math.pow(m - average, 2);
        }

        int n = measurements.size();
        double standardDeviation = Math.sqrt(varianceSum / Math.max(1, n - 1));
        double standardError = standardDeviation / Math.sqrt(n);
        double statisticalError = studentCoefficient * standardError;
        double totalError = Math.sqrt(Math.pow(systematicError, 2) + Math.pow(statisticalError, 2));

        double rounded = Math.round(totalError * 100.0) / 100.0;
        log.debug("Абсолютная погрешность: n={}, среднее={}, totalError={}", n, average, rounded);
        return rounded;
    }

    public void resetMeasurements() {
        log.info("Сброс измерений (было {} записей)", measurements.size());
        measurements.clear();
    }
}
