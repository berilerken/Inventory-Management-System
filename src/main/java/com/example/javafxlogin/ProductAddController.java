package com.example.javafxlogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductAddController implements Initializable {

    @FXML
    private TextField add_name;

    @FXML
    private TextField add_brand;

    @FXML
    private TextField add_price;

    @FXML
    private TextField add_quantity;

    @FXML
    private Label upload_url;

    @FXML
    private Button add_button;

    @FXML
    private Button upload;

    @FXML
    private Label name_valid;

    @FXML
    private Label brand_valid;

    @FXML
    private Label price_valid;

    @FXML
    private Label quantity_valid;

    List<String> lstFile;
    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    //add item button's action method
    public void addItem(){

        Connection conn = DBUtils.ProductConnection();
        String sql = "insert into product (product_name, product_brand, product_price, image) values (?,?,?,? ) ";

        if(!add_name.getText().trim().isEmpty() && !add_brand.getText().trim().isEmpty()&& !add_price.getText().trim().isEmpty()&& !upload_url.getText().trim().isEmpty()){
            try {

                pst = conn.prepareStatement(sql);
                pst.setString(1,add_name.getText());
                pst.setString(2,add_brand.getText());
                pst.setString(3,add_price.getText());
                pst.setString(4,upload_url.getText());
                pst.execute();

                System.out.println("You successfully added the item!.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Item is added successfully.");
                alert.show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{

            boolean name = DataValidation.textFieldIsNull(add_name,name_valid,"Name is required!" );
            boolean brand = DataValidation.textFieldIsNull(add_brand,brand_valid,"Brand is required!" );

            boolean isNumeric = add_price.getText().chars().allMatch( Character::isDigit );
            boolean price = DataValidation.textFieldIsNull(add_price,price_valid,"Price is required!" );

            if(!isNumeric){
                boolean price2 = DataValidation.textNumeric(add_price, price_valid, "Please only enter number!");
            }

            System.out.println("Please fill in all the information.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all information to add item!!");
            alert.show();

        }

    }


//uploading image from the pc method
    public void uploadAction(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", lstFile));
        File f = fc.showOpenDialog(null);

        if(f != null){
            upload_url.setText(f.getAbsolutePath());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lstFile = new ArrayList<>();
        lstFile.add("*.png");
        lstFile.add("*.jpg");
        lstFile.add("*.PNG");
    }
}
