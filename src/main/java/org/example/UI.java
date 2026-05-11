package org.example;
import javafx.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class UI extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage){
        this.stage = stage;
        showScreen2();
        
        stage.show();
    }

    private void showScreen1(){
        Label label = new Label("Здесь будет ваш текст");
        TextField field = new TextField("Введите текст");
        Button btn = new Button("Подтвердить");
        btn.setOnAction(e -> {
            label.setText(field.getText());
        });

        Button backbtn = new Button("Назад");
        backbtn.setOnAction(e -> showScreen2());


        VBox box = new VBox(label,field, btn,backbtn);
        stage.setScene(new Scene(box, 400,200));
    }  

    
    private void showScreen2(){
        Button firstbtn = new Button("ПЕРВОЕ");
        firstbtn.setOnAction(e -> showScreen1());

        Button secondbtn = new Button("ВТОРОЙ");
        secondbtn.setOnAction(e -> showScreen3());

        VBox box = new VBox(firstbtn,secondbtn);
        stage.setScene(new Scene(box,400,200));
    }


    private void showScreen3(){

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);


        Button backbtn = new Button("Назад");
        backbtn.setOnAction(e -> showScreen2());
        VBox box = new VBox(chart,backbtn);
        stage.setScene(new Scene(box,400,200));
    }
    

    public static void main(String[] args) {
        launch(args);
    }
    
}