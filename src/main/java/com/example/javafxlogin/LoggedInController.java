package com.example.javafxlogin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static com.example.javafxlogin.DBUtils.ProductConnection;

public class LoggedInController implements Initializable {


    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;

    @FXML
    private Button button_change;

    @FXML
    private ImageView user_img;

    @FXML
    private Button button_back;

    @FXML
    private Button button_products;

    private Boolean isAdmin;

    public LoggedInController(Boolean _isAdmin) {
        setAdmin(_isAdmin);
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean _admin) {
        isAdmin = _admin;
    }

    public LoggedInController() {
        
    }

    //Log out button's action method
    @FXML
    void handleLogOutButtonAction(ActionEvent event){
            DBUtils.changeScene(event, "hello-view.fxml", "Log in!", null,null);
    }


    @FXML
    void handleButtonAction(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("change-password.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
                stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Change Password Screen");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            System.out.println("Can't load new window.");
        }
    }


    //product button's action method
    @FXML
    void buttonAction(ActionEvent event){

        Connection conn = ProductConnection();
        ObservableList<product> list = FXCollections.observableArrayList();


        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product.fxml"));
            Parent root2 = (Parent) fxmlLoader.load();
            Node node = (Node) event.getSource();

            Stage stage = (Stage) node.getScene().getWindow();
            boolean isAdmin = (boolean)stage.getUserData();


            stage.setTitle("Product Screen");
            stage.setScene(new Scene(root2));
            stage.show();

            ProductController p = new ProductController(getAdmin());
            
        } catch (IOException e) {
            System.out.println("Can't load new window.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user_img = new ImageView(new Image(getClass().getResourceAsStream("userrr.png")));


        //back button's action method
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Log in!", null,null);
            }
        });

        //Logout button's action
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLogOutButtonAction(event);
            }
        });

    }

//to write the username on the fxml interface
    public void setUserInformation(String username){

        label_welcome.setText("Welcome " +username +"!");
    }

    public void setIsAdmin(Boolean _isAdmin){
        setAdmin(_isAdmin);
    }

}
