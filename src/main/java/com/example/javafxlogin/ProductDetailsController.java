package com.example.javafxlogin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.example.javafxlogin.DBUtils.ProductConnection;

public class ProductDetailsController  implements Initializable {
    @FXML
    private TextField update_name;

    @FXML
    private TextField update_brand;

    @FXML
    private TextField update_price;

    @FXML
    private TextField update_quantity;


    @FXML
    private ImageView product_image;

    private Image image;

    @FXML private javafx.scene.control.Button exit2;



    ObservableList<product> listM;


    private int productId = 0;


    public void setDataId(int id){
        productId = id;

        Connection conn = ProductConnection();
        PreparedStatement preparedStatement;

        try {
            listM = DBUtils.getDataProductsById(productId);
            update_name.setText(String.valueOf(listM.get(0).name));
            update_brand.setText(String.valueOf(listM.get(0).brand));
            update_price.setText(String.valueOf(listM.get(0).price));
            update_quantity.setText(String.valueOf(listM.get(0).quantity));

            showProductImage(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProductImage(int id){
        Connection conn = ProductConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement("SELECT image FROM product WHERE product_id = ?");
            preparedStatement.setString(1, String.valueOf(id));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String imageURL = "file:///" + resultSet.getString("image");
                image = new Image(imageURL, product_image.getFitWidth(), product_image.getFitHeight(),  true, true);
                product_image.setImage(image);
            }

        } catch(SQLException ex){


        }

    }

    PreparedStatement ps;
    Connection conn;
    ResultSet rs;


    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) exit2.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
