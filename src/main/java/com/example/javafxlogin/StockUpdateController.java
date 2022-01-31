package com.example.javafxlogin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.javafxlogin.DBUtils.ProductConnection;

public class StockUpdateController implements Initializable {

    @FXML
    private TextField t_quantity;
    @FXML
    private TextField stock_id;
    @FXML
    private Button update_button;

    @FXML
    private Label quantity_valid;

    ObservableList<stock> listN;

    public int stockid = 0;

    public void setStockDataId(int stockId){
        stockid = stockId;

        Connection conn = ProductConnection();
        PreparedStatement preparedStatement;

        try {
            listN = DBUtils.getDataStockbyId(stockid);
            stock_id.setText(String.valueOf(listN.get(0).stock_id));
            t_quantity.setText(String.valueOf(listN.get(0).quantity));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(){
        Connection conn = ProductConnection();
        PreparedStatement preparedStatement = null;


        if(!stock_id.getText().trim().isEmpty() && !t_quantity.getText().trim().isEmpty()){
            try {
                preparedStatement = conn.prepareStatement("UPDATE stock SET quantity= ? WHERE stock_id = ?");
                preparedStatement.setInt(1,Integer.parseInt(t_quantity.getText()));
                preparedStatement.setInt(2, stockid);


                if(Integer.parseInt(t_quantity.getText())<0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Invalid quantity number!!!!");
                    alert.show();
                }

                else{
                    preparedStatement.execute();
                    System.out.println("You successfully updated the stock!.");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Item is updated successfully.");
                    alert.show();

                    ProductController controller = new ProductController();
                    controller.refresh();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        else{

            boolean isNumeric2 = t_quantity.getText().chars().allMatch( Character::isDigit );

            boolean quantity = DataValidation.textFieldIsNull(t_quantity,quantity_valid,"Quantity is required!" );
            if(!isNumeric2){
                boolean quantity2 = DataValidation.textNumeric(t_quantity, quantity_valid, "Please only enter number!");
            }




            System.out.println("Please fill in all the information.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all information to update the product!!");
            alert.show();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
