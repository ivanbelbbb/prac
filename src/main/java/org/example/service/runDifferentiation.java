package org.example.service;

import java.util.Arrays;

import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChartBuilder;

public class runDifferentiation {

    private static void runDifferentiation()
    {

        // Исходные данные (вар 3)
        double[] t = {1.00, 1.10, 1.20, 1.30, 1.40, 1.50, 1.60, 1.70, 1.80, 1.90, 2.00};
        double[] s = {3.51, 3.19, 2.86, 2.53, 2.19, 1.85, 1.51, 1.17, 0.84, 0.51, 0.19};

        double h = 0.02;

        // интерполяция
        double[][] result = newton(t, s, h);
        double[] tn = result[0];
        double[] sn = result[1];

        // первая производная
        double[] vn = dif(sn, h);

        // вторая производная
        double[] an = dif2(sn, h);


        NevilleInterpolator interpolator = new NevilleInterpolator();
        PolynomialFunctionLagrangeForm function = interpolator.interpolate(t, s);

        
        double [][] apacheResult = apacheInterpolation(t,s, h);
        double[] apacheT = apacheResult[0];
        double [] apacheS = apacheResult[1];
        double[] apacheV = apacheDifferentiation(function, apacheT, h);
        double[] apacheA = apacheDifferentiation(function, apacheV, h);

}