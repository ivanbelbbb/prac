package org.example.service;

import static org.example.calc.Differentiation.apacheDifferentiation;
import static org.example.calc.Differentiation.apacheInterpolation;
import static org.example.calc.Differentiation.dif;
import static org.example.calc.Differentiation.dif2;
import static org.example.calc.Differentiation.newton;

import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

public class DifferentiationService {
    public record Result(
        double[] tn, double[] sn, double[] vn, double[] an,
        double[] apacheT, double[] apacheS, double[] apacheV, double[] apacheA,
        double newtonTime, double apacheTime
    ){}

    public static Result compute(double[] t, double[] s, double h){
        // // Исходные данные (вар 3)
        // double[] t = {1.00, 1.10, 1.20, 1.30, 1.40, 1.50, 1.60, 1.70, 1.80, 1.90, 2.00};
        // double[] s = {3.51, 3.19, 2.86, 2.53, 2.19, 1.85, 1.51, 1.17, 0.84, 0.51, 0.19};

        // double h = 0.02;

        // интерполяция

        long startNewton = System.nanoTime();

        double[][] result = newton(t, s, h);
        double[] tn = result[0];
        double[] sn = result[1];
        // первая производная
        double[] vn = dif(sn, h);
        // вторая производная
        double[] an = dif2(sn, h);

        long endNewton = System.nanoTime();
        double newtonTime = (endNewton - startNewton) / 1_000_000_000.0; // в секундах


        long startApache = System.nanoTime();

        NevilleInterpolator interpolator = new NevilleInterpolator();
        PolynomialFunctionLagrangeForm function = interpolator.interpolate(t, s);
        double [][] apacheResult = apacheInterpolation(t,s, h);
        double[] apacheT = apacheResult[0];
        double [] apacheS = apacheResult[1];
        double[] apacheV = apacheDifferentiation(function, apacheT, h, 1);
        double[] apacheA = apacheDifferentiation(function, apacheT, h,2);

        long endApache = System.nanoTime();
        double apacheTime = (endApache - startApache) / 1_000_000_000.0; // в секундах

        return new Result(tn, sn, vn, an, apacheT, apacheS, apacheV, apacheA, newtonTime, apacheTime);
    }

}
