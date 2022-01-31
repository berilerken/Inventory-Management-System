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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

//This class is the Change Password parts controller class
public class ChangePasswordController implements Initializable {

    @FXML
    private TextField username_tf;

    @FXML
    private TextField oldpass_tf;

    @FXML
    private TextField newpass_tf;

    @FXML
    private Button button_save;

    @FXML
    private Label username_label;

    @FXML
    private Label label_oldpass;

    @FXML
    private Label label_newpass;

    @FXML
    private ImageView lock_image;

    @FXML private javafx.scene.control.Button button_cancel;

    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //save button's action method
        button_save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                boolean username = DataValidation.textFieldIsNull(username_tf,username_label,"Username is required!" );
                boolean old_password = DataValidation.textFieldIsNull(oldpass_tf,label_oldpass,"Old Password is required!" );
                boolean new_password = DataValidation.textFieldIsNull(newpass_tf,label_newpass,"New Password is required!" );

                lock_image = new ImageView(new Image(getClass().getResourceAsStream("lock.jpeg")));

                
                if(!username_tf.getText().trim().isEmpty() && !oldpass_tf.getText().trim().isEmpty()&& !newpass_tf.getText().trim().isEmpty()){
                    DBUtils.changePassword(event, username_tf.getText(), oldpass_tf.getText(), newpass_tf.getText());
                    closeButtonAction();
                }
                else{

                    System.out.println("Please fill in all the information.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!!");
                    alert.show();
                }
            }
        });


        //cancel button's action method
        button_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeButtonAction();
            }
        });

    }



}
