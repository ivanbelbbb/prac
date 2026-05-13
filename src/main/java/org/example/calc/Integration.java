package org.example.calc;

import net.objecthunter.exp4j.Expression;

import static java.lang.Math.round;

public class Integration {

    // Метод Симпсона для численного интегрирования
    public static double simpson(Expression expr, double x1, double x2, double dx) {


        double f1 = expr.setVariable("x", x1).evaluate();
        double f2 = expr.setVariable("x", x2).evaluate();

        int n = (int) round((x2 - x1) / dx) ;
        if (n % 2 == 1) n++;  // Шаг должен быть четным
        dx = (x2 - x1) / n;

        double s = f1 + f2; // сумма
        // Интегрирование методом Симпсона
        for (double x = x1 + dx; x < x2; x += dx) {
            if (((x - x1) / dx) % 2 == 0) {
                s += 4 * expr.setVariable("x", x).evaluate();  //  для нечетных точек
            } else {
                s += 2 * expr.setVariable("x", x).evaluate();  // для четных точек
            }
        }

        return s * dx / 3;  // результат
    }
}