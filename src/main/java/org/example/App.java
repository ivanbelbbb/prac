package org.example;


import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.abs;
import static org.example.calc.Differentiation.*;
import static org.example.calc.Integration.simpson;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("выбери 1-2:");
        System.out.println("1 — дифференцирование");
        System.out.println("2 — интегрирование");
        double a = input(scanner);
        scanner.nextLine();
        if (a == 1) {
            runDifferentiation();
        } else if (a==2) {
            runIntegration(scanner);
        }
    }








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


        LinearInterpolator interpolator = new LinearInterpolator();
        PolynomialSplineFunction function = interpolator.interpolate(t, s);

        // Получаем значение в точке x = 1.25
        double value = function.value(1.25);
        System.out.println("Значение: " + value);

//        // графики
//        // Путь
//        XYChart chart1 = new XYChartBuilder().width(800).height(200).title("Путь S(t)").xAxisTitle("t, с").yAxisTitle("S, м").build();
//        chart1.addSeries("S(t)", tn, sn);
//
//        XYChart chart2 = new XYChartBuilder().width(800).height(200).title("Скорость v(t)").xAxisTitle("t, с").yAxisTitle("v, м/с").build();
//        chart2.addSeries("v(t)", tn, vn);
//
//        XYChart chart3 = new XYChartBuilder().width(800).height(200).title("Ускорение a(t)").xAxisTitle("t, с").yAxisTitle("a, м/с²").build();
//        chart3.addSeries("a(t)", tn, an);
//
//        new SwingWrapper<>(Arrays.asList(chart1, chart2, chart3)).displayChartMatrix();
    }




















    private static void runIntegration(Scanner scanner) {
        // Ввод функции от x
        System.out.print("Введите функцию от x (например, x^2): ");
        String f = scanner.nextLine();

        // Ввод пределов интегрирования

        System.out.print("Введите нижний предел x1: ");
        double x1 = input(scanner);
        System.out.println("x1 = " + x1);

        System.out.print("Введите верхний предел x2: ");
        double x2 = input(scanner);
        while (x2 < x1) {
            System.out.println("x2 должен быть больше x1");
            x2 = input(scanner);
        }
        System.out.println("x2 = " + x2);

        // Ввод шага dx
        System.out.print("Введите шаг dx: ");
        double dx = input(scanner);
        while (dx <= 0) {
            System.out.println("Шаг должен быть положительным. Попробуйте снова: ");
            dx = input(scanner);
        }
        System.out.println("dx = " + dx);

        // таймер начало
        long startTime = System.nanoTime();

        // Вызов метода Симпсона и вывод результата
        double result1 = simpson(f, x1, x2, dx);
        double result2 = simpson(f, x1, x2, dx / 2);
        double E = abs((result2 - result1)) / 15;

        // таймер конец
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000_000.0; // в секундах

        System.out.println("Результат интегрирования: " + result1);
        System.out.println("Результат интегрирования dx/2: " + result2);
        System.out.println("Погрешность по Рунге: " + E);
        System.out.printf("Время выполнения вычислений: %.3f секунд%n", executionTime);
    }

    private static double input(Scanner scanner){
        while (true){
            if (scanner.hasNextDouble()){
                return scanner.nextDouble();
            }else{
                System.out.println("ERROR: На вход требуется число. Повторите попыткку: ");
                scanner.next();
            }
        }
    }
}