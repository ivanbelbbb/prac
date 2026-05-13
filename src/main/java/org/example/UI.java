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

        btnDiff.setMaxWidth(Double.MAX_VALUE);
        btnInteg.setMaxWidth(Double.MAX_VALUE);

        btnDiff.setOnAction(e -> showDifferentiation());
        btnInteg.setOnAction(e -> showIntegration());

        VBox box = new VBox(16, title, btnDiff, btnInteg);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(32));

        stage.setScene(new Scene(box, 420, 220));
    }

    private void showDifferentiation() {
        double[] t = {1.00, 1.10, 1.20, 1.30, 1.40, 1.50, 1.60, 1.70, 1.80, 1.90, 2.00};
        double[] s = {3.51, 3.19, 2.86, 2.53, 2.19, 1.85, 1.51, 1.17, 0.84, 0.51, 0.19};
        double h = 0.02;

        DifferentiationService.Result res = DifferentiationService.compute(t, s, h);

        LineChart<Number, Number> chartS = buildChart("Путь S(t)", "t", "S");
        addSeries(chartS, "Ньютон", res.tn(), res.sn());
        addSeries(chartS, "Apache", res.apacheT(), res.apacheS());

        LineChart<Number, Number> chartV = buildChart("Скорость v(t)", "t", "v");
        addSeries(chartV, "Ньютон", res.tn(), res.vn());
        addSeries(chartV, "Apache", res.apacheT(), res.apacheV());

        LineChart<Number, Number> chartA = buildChart("Ускорение a(t)", "t", "a");
        addSeries(chartA, "Ньютон", res.tn(), res.an());
        addSeries(chartA, "Apache", res.apacheT(), res.apacheA());

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        GridPane.setColumnSpan(chartS, 2);
        grid.add(chartS,0,0);
        grid.add(chartV, 0, 1);
        grid.add(chartA, 1, 1);

        Button back = new Button("Назад");
        back.setOnAction(e -> showMenu());

        VBox box = new VBox(16, grid, back);
        stage.setScene(new Scene(box, 1000, 1000));
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

        Button calcButton = new Button("Вычислить");
        calcButton.setOnAction(e -> {
        try {
            String f = expField.getText();
            if (f.isEmpty()) { resultLabel.setText("Ошибка: введите функцию"); return; }

            double x1 = Double.parseDouble(x1Field.getText());
            double x2 = Double.parseDouble(x2Field.getText());
            double dx = Double.parseDouble(dxField.getText());

            if (x2 <= x1) { resultLabel.setText("Ошибка: x2 должен быть больше x1"); return; }
            if (dx <= 0)  { resultLabel.setText("Ошибка: шаг должен быть положительным"); return; }

            IntegrationService.Result res = IntegrationService.compute(f, x1, x2, dx);
            resultLabel.setText(
                "Мой Симпсон (dx):   " + res.dxValue() + "\n" +
                "Мой Симпсон (dx/2): " + res.dx2Value() + "\n" +
                "Погрешность Рунге:  " + res.runge() + "\n" +
                "Время моего:        " + res.simpsonTime() + " с\n" +
                "Apache результат:   " + res.apacheValue() + "\n" +
                "Время Apache:       " + res.apacheTime() + " с"
            );
        } catch (NumberFormatException ex) {
            resultLabel.setText("Ошибка: введите числа в поля x1, x2, dx");
        } catch (Exception ex) {
            resultLabel.setText("Ошибка: " + ex.getMessage());
        }
    });

        Button backButton = new Button("назад");
        backButton.setOnAction(e -> showMenu());

        VBox box = new VBox(8, title, lblF, expField, lblX1, x1Field, lblX2, x2Field, lblDx, dxField, calcButton, resultLabel, backButton);
        box.setPadding(new Insets(16));
        stage.setScene(new Scene(box, 420, 500));
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

    public static void main(String[] args) {
        launch(args);
    }
}