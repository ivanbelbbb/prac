package org.example;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.util.Arrays;

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

    public static void main(String[] args) {
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

        // графики
        // Путь
        XYChart chart1 = new XYChartBuilder().width(800).height(200).title("Путь S(t)").xAxisTitle("t, с").yAxisTitle("S, м").build();
        chart1.addSeries("S(t)", tn, sn);

        XYChart chart2 = new XYChartBuilder().width(800).height(200).title("Скорость v(t)").xAxisTitle("t, с").yAxisTitle("v, м/с").build();
        chart2.addSeries("v(t)", tn, vn);

        XYChart chart3 = new XYChartBuilder().width(800).height(200).title("Ускорение a(t)").xAxisTitle("t, с").yAxisTitle("a, м/с²").build();
        chart3.addSeries("a(t)", tn, an);

        new SwingWrapper<>(Arrays.asList(chart1, chart2, chart3)).displayChartMatrix();
    }
}