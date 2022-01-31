package com.example.javafxlogin;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.javafxlogin.DBUtils.ProductConnection;

public class ProductStockController implements Initializable {


    @FXML
    private Button button_ref;

    @FXML
    private Button button_exit;

    @FXML
    private Button stock_update;


    @FXML
    private TextField search_name;

    @FXML
    private TextField search_brand;


    @FXML
    private TableColumn<stock,Integer> col_id;

    @FXML
    private TableColumn<stock,String> col_name;

    @FXML
    private TableColumn<stock,String> col_brand;

    @FXML
    private TableColumn<stock,Integer> col_price;

    @FXML
    private TableColumn<stock,Integer> col_quantity;

    @FXML
    private TableColumn<stock,String> col_location;

    @FXML
    private TableView<stock> table_product;

    ObservableList<stock> listN;
    ObservableList<stock> dataList1;

    @FXML
    void searchName(){
        col_id.setCellValueFactory(new PropertyValueFactory<stock, Integer>("stock_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<stock, String>("product_name"));
        col_brand.setCellValueFactory(new PropertyValueFactory<stock, String>("product_brand"));
        col_price.setCellValueFactory(new PropertyValueFactory<stock, Integer>("product_price"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<stock, Integer>("quantity"));
        col_location.setCellValueFactory(new PropertyValueFactory<stock, String>("location"));

        dataList1 = DBUtils.getDataStock();
        table_product.setItems(dataList1);
        FilteredList<stock> filteredData = new FilteredList<>(dataList1, b -> true);
        search_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(product.getProduct_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_product.comparatorProperty());
        table_product.setItems(sortedData);
    }

    @FXML
    void searchBrand(){
        col_id.setCellValueFactory(new PropertyValueFactory<stock, Integer>("stock_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<stock, String>("product_name"));
        col_brand.setCellValueFactory(new PropertyValueFactory<stock, String>("product_brand"));
        col_price.setCellValueFactory(new PropertyValueFactory<stock, Integer>("product_price"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<stock, Integer>("quantity"));
        col_location.setCellValueFactory(new PropertyValueFactory<stock, String>("location"));

        dataList1 = DBUtils.getDataStock();
        table_product.setItems(dataList1);
        FilteredList<stock> filteredData = new FilteredList<>(dataList1, b -> true);
        search_brand.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(product.getProduct_brand().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_product.comparatorProperty());
        table_product.setItems(sortedData);
    }

    public void refresh(){

        try {
            listN = DBUtils.getDataStock();
            table_product.setItems(listN);

            searchName();
            searchBrand();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Table is refreshed!");
            alert.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) button_exit.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void stockUpdate(ActionEvent event){

        try{

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stock-update.fxml"));
            Parent root3 = fxmlLoader.load();
            Node node = (Node) event.getSource();

            Stage stageScene = new Stage();
            Stage stage = (Stage) node.getScene().getWindow();

                Stage stage2 = new Stage();
                stage2.initStyle(StageStyle.DECORATED);
                stage2.setTitle("Stock Update Screen");
                stage2.setScene(new Scene(root3));

                StockUpdateController controller = fxmlLoader.getController();
                controller.setStockDataId(table_product.getSelectionModel().getSelectedItem().getStock_id());



                stage2.show();
                searchName();
                searchBrand();


        } catch (IOException e) {
            System.out.println("Can't load new window.");
        }
            searchName();
            searchBrand();

    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        col_id.setCellValueFactory(new PropertyValueFactory<stock, Integer>("stock_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<stock, String>("product_name"));
        col_brand.setCellValueFactory(new PropertyValueFactory<stock, String>("product_brand"));
        col_price.setCellValueFactory(new PropertyValueFactory<stock, Integer>("product_price"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<stock, Integer>("quantity"));
        col_location.setCellValueFactory(new PropertyValueFactory<stock, String>("location"));

        listN = DBUtils.getDataStock();
        table_product.setItems(listN);

    }
}
