package com.example.javafxlogin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//the main class of our project the application will start with this class
public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        primaryStage.setTitle("Log in!");
        primaryStage.setScene(new Scene(root, 1015, 606));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}