package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.example.service.DifferentiationService;
import org.example.service.IntegrationService;

public class UI extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        showMenu();
        stage.setTitle("Численные методы");
        stage.show();
    }

    private void showMenu() {
        Label title = new Label("Выберите задачу");

        Button btnDiff = new Button("Дифференцирование");
        Button btnInteg = new Button("Интегрирование");
        Button btnExit = new Button("Выход");
        Button btnInfo = new Button("Информация о программе и авторе");

        btnDiff.setMaxWidth(Double.MAX_VALUE);
        btnInteg.setMaxWidth(Double.MAX_VALUE);
        btnInfo.setMaxWidth(Double.MAX_VALUE); 
        

        btnDiff.setOnAction(e -> showDifferentiation());
        btnInteg.setOnAction(e -> showIntegration());

        btnExit.setMaxWidth(Double.MAX_VALUE);
        btnExit.setOnAction(e -> javafx.application.Platform.exit());

        btnInfo.setOnAction(e -> showInfo());

        VBox box = new VBox(16, title, btnDiff, btnInteg, btnInfo, btnExit);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(32));

        stage.setScene(new Scene(box, 420, 280));
    }

    private void showInfo() {
        Label title = new Label("Информация о программе и авторе");

        Label info = new Label(
            "Название программы: Численные методы\n\n" +
            "Назначение: анализ и визуализация числовой информации\n" +
            "с применением численных методов.\n\n" +
            "Реализованные методы:\n" +
            "  - Интегрирование — метод Симпсона\n" +
            "  - Дифференцирование — интерполяция Ньютона, численное дифференцирование\n" +
            "Автор: Беляев Иван Вячеславович\n" +
            "Группа: 4531\n" +
            "Направление: 09.03.04 — Программная инженерия\n" +
            "Курс: 1\n" +
            "Вуз: Санкт-Петербургский государственный университет\n" +
            "аэрокосмического приборостроения (ГУАП)\n\n" +
            "Контакты: imthe3784@gmail.com"
        );
        info.setWrapText(true);

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> showMenu());

        VBox box = new VBox(16, title, info, backButton);
        box.setPadding(new Insets(16));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);

        stage.setScene(new Scene(scrollPane, 1000, 900));
    }

    private void showDifferentiation() {
        Label title = new Label("Дифференцирование");
        Label resultLabel = new Label();
        resultLabel.setWrapText(true);

        Label lblT = new Label("Массив t (через пробел):");
        TextField tField = new TextField();

        Label lblS = new Label("Массив s (через пробел):");
        TextField sField = new TextField();

        Label lblH = new Label("Шаг h:");
        TextField hField = new TextField("0.02");

        Button testButton = new Button("Тестовые данные");
        Button calcButton = new Button("Посчитать");
        Button backButton = new Button("Назад");

        backButton.setOnAction(e -> showMenu());

        VBox chartsBox = new VBox(10);

        testButton.setOnAction(e -> {
            tField.setText("1.00 1.10 1.20 1.30 1.40 1.50 1.60 1.70 1.80 1.90 2.00");
            sField.setText("3.51 3.19 2.86 2.53 2.19 1.85 1.51 1.17 0.84 0.51 0.19");
            hField.setText("0.02");
            resultLabel.setText("Тестовые данные загружены");
        });

        calcButton.setOnAction(e -> {
            try {
                double[] t = parseArray(tField.getText());
                double[] s = parseArray(sField.getText());
                double h = Double.parseDouble(hField.getText());

                if (t.length == 0 || s.length == 0) {
                    resultLabel.setText("Ошибка: массивы не должны быть пустыми");
                    return;
                }
                if (t.length != s.length) {
                    resultLabel.setText("Ошибка: размеры массивов не совпадают");
                    return;
                }
                if (t.length < 3) {
                    resultLabel.setText("Ошибка: нужно минимум 3 точки");
                    return;
                }
                for (int i = 1; i < t.length; i++) {
                    if (t[i] <= t[i - 1]) {
                        resultLabel.setText("Ошибка: узлы t должны строго возрастать");
                        return;
                    }
                }
                if (h <= 0) {
                    resultLabel.setText("Ошибка: шаг h должен быть положительным");
                    return;
                }
                if (h >= (t[t.length - 1] - t[0])) {
                    resultLabel.setText("Ошибка: шаг h больше диапазона данных");
                    return;
                }

                DifferentiationService.Result res = DifferentiationService.compute(t, s, h);

                LineChart<Number, Number> chartS = buildChart("Путь S(t)", "t", "S");
                addSeries(chartS, "Ньютон", res.tn(), res.sn());
                addSeries(chartS, "Apache", res.apacheT(), res.apacheS());
                chartS.setPrefHeight(300);

                LineChart<Number, Number> chartV = buildChart("Скорость v(t)", "t", "v");
                addSeries(chartV, "Ньютон", res.tn(), res.vn());
                addSeries(chartV, "Apache", res.apacheT(), res.apacheV());
                chartV.setPrefHeight(250);

                LineChart<Number, Number> chartA = buildChart("Ускорение a(t)", "t", "a");
                addSeries(chartA, "Ньютон", res.tn(), res.an());
                addSeries(chartA, "Apache", res.apacheT(), res.apacheA());
                chartA.setPrefHeight(250);

                GridPane grid = new GridPane();
                grid.setHgap(8);
                grid.setVgap(8);
                GridPane.setColumnSpan(chartS, 2);
                grid.add(chartS, 0, 0);
                grid.add(chartV, 0, 1);
                grid.add(chartA, 1, 1);

                chartsBox.getChildren().clear();
                chartsBox.getChildren().add(grid);

                resultLabel.setText(
                    "Время Ньютона: " + res.newtonTime() + " с\n" +
                    "Время Apache:  " + res.apacheTime()  + " с"
                );

            } catch (NumberFormatException ex) {
                resultLabel.setText("Ошибка: введите числа в поля массивов и шага");
            } catch (Exception ex) {
                resultLabel.setText("Ошибка: " + ex.getMessage());
            }
        });

        VBox box = new VBox(10,
            title,
            lblT, tField,
            lblS, sField,
            lblH, hField,
            testButton, calcButton,
            chartsBox,
            resultLabel,
            backButton);
        box.setPadding(new Insets(16));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);

        stage.setScene(new Scene(scrollPane, 1000, 900));
    }

    private void showIntegration() {
        Label title = new Label("Интегрирование");
        Label lblF  = new Label("Функция f(x):");
        TextField expField = new TextField();
        Label lblX1 = new Label("x1:");
        TextField x1Field = new TextField();
        Label lblX2 = new Label("x2:");
        TextField x2Field = new TextField();
        Label lblDx = new Label("dx:");
        TextField dxField = new TextField();
        Label resultLabel = new Label();
        resultLabel.setWrapText(true);

        Button testButton = new Button("Тестовые данные");
        testButton.setOnAction(e -> {
            expField.setText("(x^3 -1)/(x^3 -3*x^2 +1)");
            x1Field.setText("1");
            x2Field.setText("2");
            dxField.setText("0.05");
            resultLabel.setText("Тестовые данные загружены");
        });

        Button calcButton = new Button("Вычислить");
        calcButton.setOnAction(e -> {
            try {
                String f = expField.getText();
                if (f.isEmpty()) {
                    resultLabel.setText("Ошибка: введите функцию");
                    return;
                }

                try {
                    new ExpressionBuilder(f).variable("x").build()
                        .setVariable("x", 1.0).evaluate();
                } catch (Exception ex) {
                    resultLabel.setText("Ошибка: некорректное математическое выражение");
                    return;
                }

                double x1 = Double.parseDouble(x1Field.getText());
                double x2 = Double.parseDouble(x2Field.getText());
                double dx = Double.parseDouble(dxField.getText());

                if (x2 <= x1) {
                    resultLabel.setText("Ошибка: x2 должен быть больше x1");
                    return;
                }
                if (dx <= 0) {
                    resultLabel.setText("Ошибка: шаг должен быть положительным");
                    return;
                }
                if (dx >= (x2 - x1)) {
                    resultLabel.setText("Ошибка: шаг больше интервала интегрирования");
                    return;
                }
                double n = (x2 - x1) / dx;
                if (Math.abs(n - Math.round(n)) > 1e-10) {
                    resultLabel.setText("Ошибка: шаг должен делить интервал на равные участки");
                    return;
                }
                if (((int) Math.round(n)) % 2 != 0) {
                    resultLabel.setText("Ошибка: метод Симпсона требует чётное число интервалов");
                    return;
                }

                IntegrationService.Result res = IntegrationService.compute(f, x1, x2, dx);

                String apacheStr = Double.isNaN(res.apacheValue())
                    ? "не удалось вычислить (разрыв на промежутке)"
                    : String.valueOf(res.apacheValue());

                resultLabel.setText(
                    "Метод Симпсона (dx):   " + res.dxValue()    + "\n" +
                    "Метод Симпсона (dx/2): " + res.dx2Value()   + "\n" +
                    "Погрешность Рунге:     " + res.runge()      + "\n" +
                    "Время Симпсона:        " + res.simpsonTime() + " с\n" +
                    "Apache результат:      " + apacheStr        + "\n" +
                    "Время Apache:          " + res.apacheTime()  + " с"
                );

            } catch (NumberFormatException ex) {
                resultLabel.setText("Ошибка: введите числа в поля x1, x2, dx");
            } catch (Exception ex) {
                resultLabel.setText("Ошибка: " + ex.getMessage());
            }
        });

        Button backButton = new Button("Назад");
        backButton.setOnAction(e -> showMenu());

        VBox box = new VBox(8,
            title,
            lblF, expField,
            lblX1, x1Field,
            lblX2, x2Field,
            lblDx, dxField,
            testButton,
            calcButton,
            resultLabel,
            backButton);
        box.setPadding(new Insets(16));

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);

        stage.setScene(new Scene(scrollPane, 1000, 900));
    }

    private LineChart<Number, Number> buildChart(String title, String xLabel, String yLabel) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setCreateSymbols(false);
        return chart;
    }

    private void addSeries(LineChart<Number, Number> chart, String name, double[] x, double[] y) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int i = 0; i < x.length; i++) {
            series.getData().add(new XYChart.Data<>(x[i], y[i]));
        }
        chart.getData().add(series);
    }

    private double[] parseArray(String text) {
        String[] parts = text.trim().split("[,\\s]+");
        double[] arr = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Double.parseDouble(parts[i]);
        }
        return arr;
    }

    public static void main(String[] args) {
        launch(args);
    }
}