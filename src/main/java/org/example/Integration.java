package org.example;

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.Expression;
import java.util.Scanner;
import static java.lang.Math.abs;
import static java.lang.Math.round;

public class Integration {

    // Метод Симпсона для численного интегрирования
    public static double simpson(String func, double x1, double x2, double dx) {
        // объект, который преобразует строку в функцию
        Expression expr = new ExpressionBuilder(func)
                .variable("x")
                .build();

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

    public static double input(Scanner scanner){
        while (true){
            if (scanner.hasNextDouble()){
                return scanner.nextDouble();
            }else{
                System.out.println("ERROR: На вход требуется число. Повторите попыткку: ");
                scanner.next();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод функции от x
        System.out.print("Введите функцию от x (например, x^2): ");
        String f = scanner.nextLine();

        // Ввод пределов интегрирования

        System.out.print("Введите нижний предел x1: ");
        double x1 = input(scanner);
        System.out.println("x1 = " + x1);

        System.out.print("Введите верхний предел x2: ");
        double x2 = input(scanner);
        while (x2 < x1){
            System.out.println("x2 должен быть больше x1");
            x2 = input(scanner);
        }
        System.out.println("x2 = " + x2);

        // Ввод шага dx
        System.out.print("Введите шаг dx: ");
        double dx = input(scanner);
        while (dx <= 0){
            System.out.println("Шаг должен быть положительным. Попробуйте снова: ");
            dx = input(scanner);
        }
        System.out.println("dx = " + dx);

        // таймер начало
        long startTime = System.nanoTime();

        // Вызов метода Симпсона и вывод результата
        double result1 = simpson(f, x1, x2, dx);
        double result2 = simpson(f, x1, x2, dx/2);
        double E = abs((result2 - result1))/15;

        // таймер конец
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000_000.0; // в секундах

        System.out.println("Результат интегрирования: " + result1);
        System.out.println("Результат интегрирования dx/2: " + result2);
        System.out.println("Погрешность по Рунге: " + E);
        System.out.printf("Время выполнения вычислений: %.3f секунд%n", executionTime);
    }
}