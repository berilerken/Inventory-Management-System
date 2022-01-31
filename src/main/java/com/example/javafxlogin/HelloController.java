package com.example.javafxlogin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private Button button_exit;

    @FXML
    private ImageView tf_userimage;

    @FXML
    private ImageView tf_lock;

    @FXML
    private Label username_label;

    @FXML
    private Label password_label;


    //Close button's action method
    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) button_exit.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //login button's action method
        button_login.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {
                boolean username = DataValidation.textFieldIsNull(tf_username,username_label,"Username is required!" );
                boolean password = DataValidation.textFieldIsNull(tf_password,password_label,"Password is required!" );
                tf_userimage = new ImageView(new Image(getClass().getResourceAsStream("userrr.png")));
                tf_lock = new ImageView(new Image(getClass().getResourceAsStream("lock.jpeg")));
                DBUtils.logInUser(event, tf_username.getText(), tf_password.getText());

            }
        });

        //Exit button's action method
        button_exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeButtonAction();
            }
        });

        //Signup button's action method
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", "Sign up!", null,null);
            }
        });
    }
}