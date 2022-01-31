package com.example.javafxlogin;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.javafxlogin.DBUtils.ProductConnection;

public class ProductUpdateController implements Initializable {
    @FXML
    private TextField update_name;

    @FXML
    private TextField update_brand;

    @FXML
    private TextField update_price;

    @FXML
    private TextField update_quantity;

    @FXML
    private Button update;


    @FXML
    private Label name_valid;

    @FXML
    private Label brand_valid;

    @FXML
    private Label price_valid;

    @FXML
    private Label quantity_valid;

    @FXML private javafx.scene.control.Button exit;



    ObservableList<product> listM;


   public void update(){
        Connection conn = ProductConnection();
        PreparedStatement preparedStatement = null;


       if(!update_name.getText().trim().isEmpty() && !update_brand.getText().trim().isEmpty()&& !update_price.getText().trim().isEmpty()){
           try {
               preparedStatement = conn.prepareStatement("UPDATE product SET product_name= ?, product_brand= ?, product_price= ? WHERE product_id = ?");
               preparedStatement.setString(1, update_name.getText());
               preparedStatement.setString(2, update_brand.getText());
               preparedStatement.setInt(3,Integer.parseInt(update_price.getText()));
               preparedStatement.setInt(4, productId);
               preparedStatement.execute();

               System.out.println("You successfully updated the item!.");
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setContentText("Item is updated successfully.");
               alert.show();


               ProductController controller = new ProductController();
               controller.refresh();

           } catch (SQLException e) {
               e.printStackTrace();
           }
       }

       else{

           boolean name = DataValidation.textFieldIsNull(update_name,name_valid,"Name is required!" );
           boolean brand = DataValidation.textFieldIsNull(update_brand,brand_valid,"Brand is required!" );
           boolean isNumeric = update_price.getText().chars().allMatch( Character::isDigit );
           boolean price = DataValidation.textFieldIsNull(update_price,price_valid,"Price is required!" );

           if(!isNumeric){
               boolean price2 = DataValidation.textNumeric(update_name, price_valid, "Please only enter number!");
           }


           System.out.println("Please fill in all the information.");
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText("Please fill in all information to update the product!!");
           alert.show();
       }


    }

    private int productId = 0;

   public void setUpdateDataId(int id){
       productId = id;

       Connection conn = ProductConnection();
       PreparedStatement preparedStatement;

       try {
           listM = DBUtils.getDataProductsById(productId);
           update_name.setText(String.valueOf(listM.get(0).name));
           update_brand.setText(String.valueOf(listM.get(0).brand));
           update_price.setText(String.valueOf(listM.get(0).price));
           update_quantity.setText(String.valueOf(listM.get(0).quantity));



       } catch (Exception e) {
           e.printStackTrace();
       }
   }


    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
