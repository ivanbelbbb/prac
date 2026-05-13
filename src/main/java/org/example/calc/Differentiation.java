package org.example.calc;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

public class Differentiation {
    // интерполяция
    public static double[][] newton(double[] t, double[] s, double step) {

    int n = t.length;
    double[] dd = new double[n];
    // копируем значения
    for (int i = 0; i < n; i++) {
        dd[i] = s[i];
    }
    // считаем разделённые разности
    for (int j = 1; j < n; j++) {
        for (int i = n - 1; i >= j; i--) {
            dd[i] = (dd[i] - dd[i - 1]) / (t[i] - t[i - j]);
        }
    }
    // строим сгущённую сетку
    int m = (int)((t[n - 1] - t[0]) / step) + 1;
    double[] tn = new double[m];
    double[] sn = new double[m];
    
    for (int i = 0; i < m; i++) {
        tn[i] = t[0] + i * step;
    }
    // считаем значение полинома в каждой точке
    for (int i = 0; i < m; i++) {
        double x = tn[i];
        double result = dd[n - 1];
        for (int j = n - 2; j >= 0; j--) {
            result = result * (x - t[j]) + dd[j];
        }        
        sn[i] = result;
    }
    return new double[][] {tn, sn}; 
    }

    // производная
    public static double[] dif(double[] y, double h) {
        double[] dy = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            if (i == 0) {
                // для крайних точек
                dy[i] = (y[i+1] - y[i]) / h;
            } else if (i == y.length-1) {
                // для крайних точек
                dy[i] = (y[i] - y[i-1]) / h;
            } else {
                // для центральных точек
                dy[i] = (y[i+1] - y[i-1]) / (2 * h);
            }
        }
        return dy;
    }

    // вторая производная
    public static double[] dif2(double[] y, double h) {
        double[] d2y = new double[y.length];
        for (int i = 1; i < y.length -1; i++) {
            d2y[i] = (y[i+1] - 2*y[i] + y[i-1]) / (h * h);
        }
        d2y[0] = (y[2] - 2*y[1] + y[0]) / (h * h);
        d2y[y.length-1] = (y[y.length-1] - 2*y[y.length-2] + y[y.length-3]) / (h * h);
        return d2y;
    }


    // APACHE COMMONS MATH3 ИНТЕРПОЛЯЦИЯ
    public static double[][] apacheInterpolation(double[] t, double[] s, double h) {
        NevilleInterpolator interpolator = new NevilleInterpolator();
        PolynomialFunctionLagrangeForm polynomial = (PolynomialFunctionLagrangeForm) interpolator.interpolate(t, s);

        int n = (int)((t[t.length - 1] - t[0]) / h) + 1;
        double[] tn = new double[n];
        double[] sn = new double[n];

        for (int i = 0; i < n; i++) {
            tn[i] = t[0] + i * h;
            sn[i] = polynomial.value(tn[i]);
        }

        return new double[][] {tn, sn};
    }

    // APACHE COMMONS MATH3 ДИФФЕРЕНЦИРОВАНИЕ
    public static double[] apacheDifferentiation(PolynomialFunctionLagrangeForm polynomial, double[] t, double h, int order) {
    FiniteDifferencesDifferentiator diff = new FiniteDifferencesDifferentiator(5, h);
    UnivariateDifferentiableFunction d = diff.differentiate(polynomial);
    double[] dy = new double[t.length];
    for (int i = 0; i < t.length; i++) {
        DerivativeStructure x = new DerivativeStructure(1, order, 0, t[i]);
        dy[i] = d.value(x).getPartialDerivative(order);
    }
    return dy;
    }
}