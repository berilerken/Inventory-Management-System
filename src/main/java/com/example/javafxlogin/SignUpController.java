package com.example.javafxlogin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class

SignUpController implements Initializable {

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    @FXML
    private Button button_log_in;

    @FXML
    private Button button_signup;

    @FXML
    private ImageView tf_userimage1;

    @FXML
    private Label username_label;

    @FXML
    private Label pass_label;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean username = DataValidation.textFieldIsNull(tf_username,username_label,"Username is required!" );
                boolean password = DataValidation.textFieldIsNull(tf_password,pass_label,"Password is required!" );
                tf_userimage1 = new ImageView(new Image(getClass().getResourceAsStream("userrr.png")));
                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    DBUtils.signUpUser(event, tf_username.getText(), tf_password.getText());
                }
                else{
                    System.out.println("Please fill in all the information.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!!");
                    alert.show();
                }
            }
        });


        button_log_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Log in!", null,null);
            }
        });

    }
}
