package org.example.service;

import net.objecthunter.exp4j.Expression;
import static org.example.calc.Integration.simpson;
import static java.lang.Math.abs;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

import net.objecthunter.exp4j.ExpressionBuilder;

public class IntegrationService {
    public record Result(double dxValue, double dx2Value, double runge, double simpsonTime, double apacheTime, double apacheValue) {}

    public static Result compute(String f, double x1, double x2, double dx){
        Expression expr = new ExpressionBuilder(f)
        .variable("x")
        .build();

        
        long startSimpson = System.nanoTime();
        double dxValue = simpson(expr, x1, x2, dx);
        long endSimpson = System.nanoTime();
        double simpsonTime = (endSimpson - startSimpson) / 1_000_000_000.0; // в секундах

        double dx2Value = simpson(expr, x1, x2, dx/2);
        double runge = abs((dx2Value - dxValue)) / 15;


        long startApache = System.nanoTime();
        UnivariateIntegrator apacheSimpson = new SimpsonIntegrator();
        double apacheValue = apacheSimpson.integrate(10000, x -> expr.setVariable("x", x).evaluate(), x1, x2);
        long endApache = System.nanoTime();
        double apacheTime = (endApache - startApache) / 1_000_000_000.0; // в секундах

        return new Result(dxValue, dx2Value, runge, simpsonTime, apacheTime,apacheValue);
    }
}
