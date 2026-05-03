package org.example.calc;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

public class Differentiation {
    // интерполяция
    public static double[][] newton(double[] t, double[] s, double step) {
        int n = (int)((t[t.length-1] - t[0]) / step) + 1;
        double[] tn = new double[n];
        double[] sn = new double[n];

        for (int i = 0; i < n; i++) {
            tn[i] = t[0] + i * step;
        }

        // находим опорные точки(для полинома 3-ей степени достаточно 4)
        for (int i = 0; i < n; i++) {
            double x = tn[i];
            int k = 0;
            for (int j = 0; j < t.length - 3; j++) {
                if (x >= t[j] && x <= t[j+3]) {
                    k = j;
                    break;
                }
            }

            // запоминаем значения
            double x0 = t[k], x1 = t[k+1], x2 = t[k+2], x3 = t[k+3];
            double y0 = s[k], y1 = s[k+1], y2 = s[k+2], y3 = s[k+3];

            // разделенные разности
            double d1_0 = (y1 - y0) / (x1 - x0);
            double d1_1 = (y2 - y1) / (x2 - x1);
            double d1_2 = (y3 - y2) / (x3 - x2);

            double d2_0 = (d1_1 - d1_0) / (x2 - x0);
            double d2_1 = (d1_2 - d1_1) / (x3 - x1);

            double d3 = (d2_1 - d2_0) / (x3 - x0);

            // собираем полином и считаем значения сгущенных точек
            sn[i] = y0 + (x-x0)*d1_0 + (x-x0)*(x-x1)*d2_0 + (x-x0)*(x-x1)*(x-x2)*d3;
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
    public static double[] apacheDifferntiation(PolynomialFunctionLagrangeForm polynomial,double[] t, double h){
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return polynomial.value(x);
            }
        }
        FiniteDifferencesDifferentiator diff = new FiniteDifferencesDifferentiator(5, h);
        double[] dy = new double[t.length];
        UnivariateFunction derivative = diff.differentiate(polynomial);
        for (int i = 0; i < t.length; i++){
            dy[i] = derivative.value(t[i]);
        }
        return dy;
    }
}