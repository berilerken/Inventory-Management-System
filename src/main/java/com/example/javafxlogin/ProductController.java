package com.example.javafxlogin;

import javafx.collections.FXCollections;
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
import javafx.scene.control.Button;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.javafxlogin.DBUtils.ProductConnection;
import static com.example.javafxlogin.DBUtils.deleteProduct;


public class ProductController implements Initializable {


    @FXML
    private Button button_add;

    @FXML
    private Button button_del;

    @FXML
    private Button button_stock;
    @FXML
    private Button button_upt;

    @FXML
    private Button button_ref;

    @FXML
    private Button button_details;

    @FXML
    private Button button_exit;

    @FXML
    private Button button_search;

    @FXML
    private TextField search_name;

    @FXML
    private TextField search_brand;


    @FXML
    private TableColumn<product,Integer> col_id;

    @FXML
    private TableColumn<product,String> col_name;

    @FXML
    private TableColumn<product,String> col_brand;

    @FXML
    private TableColumn<product,Integer> col_price;

    @FXML
    private TableColumn<product,Integer> col_quantity;

    @FXML
    private TableColumn<product, Blob> image;

    @FXML
    private TableView<product> table_product;

    private FileInputStream fis;

    private Boolean isAdmin ;

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    //Constructors
    public ProductController(Boolean _isAdmin) {
        setAdmin(_isAdmin);
    }

    public ProductController() {
    }
    ObservableList<product> listM;

    ObservableList<product> dataList;

    ObservableList<stock> listN;

    ObservableList<product> dataList1;

    int index = -1;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    private Alert alert;
    private ActionEvent event;

    //search field for the filter part by name
    @FXML
    void searchName(){
        col_id.setCellValueFactory(new PropertyValueFactory<product, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<product, String>("name"));
        col_brand.setCellValueFactory(new PropertyValueFactory<product, String>("brand"));
        col_price.setCellValueFactory(new PropertyValueFactory<product, Integer>("price"));

        dataList = DBUtils.getDataProducts();
        table_product.setItems(dataList);
        FilteredList<product> filteredData = new FilteredList<>(dataList, b -> true);
        search_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(product.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_product.comparatorProperty());
        table_product.setItems(sortedData);
    }

    //search field for the filter part by brand
    @FXML
    void searchBrand(){
        col_id.setCellValueFactory(new PropertyValueFactory<product, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<product, String>("name"));
        col_brand.setCellValueFactory(new PropertyValueFactory<product, String>("brand"));
        col_price.setCellValueFactory(new PropertyValueFactory<product, Integer>("price"));

        dataList = DBUtils.getDataProducts();
        table_product.setItems(dataList);
        FilteredList<product> filteredData = new FilteredList<>(dataList, b -> true);
        search_brand.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(product.getBrand().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_product.comparatorProperty());
        table_product.setItems(sortedData);
    }

    //product add button's action method
    @FXML
    void buttonAction(ActionEvent event){
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product-add.fxml"));
            Parent root2 = (Parent) fxmlLoader.load();
            Node node = (Node) event.getSource();

            Stage stageScene = new Stage();
            Stage stage = (Stage) node.getScene().getWindow();
            boolean isAdmin = (boolean)stage.getUserData();
            if(isAdmin){
                stageScene.setTitle("Product Add Screen");
                stageScene.setScene(new Scene(root2));
                stageScene.show();

            }
           else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("You are not allowed to add!");
                alert.show();
            }

        } catch (IOException e) {
            System.out.println("Can't load new product window.");
        }
    }


    //refres button's method
    public void refresh(){

        try {
            listM = DBUtils.getDataProducts();
            table_product.setItems(listM);

            searchName();
            searchBrand();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Table is refreshed!");
            alert.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //delete button's method
    @FXML
    private void deleteAction(ActionEvent event){
        Node node = (Node) event.getSource();

        Stage stageScene = new Stage();
        Stage stage = (Stage) node.getScene().getWindow();
        boolean isAdmin = (boolean)stage.getUserData();
         if(isAdmin) {
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.getButtonTypes().remove(ButtonType.OK);
             alert.getButtonTypes().add(ButtonType.CANCEL);
             alert.getButtonTypes().add(ButtonType.YES);
             alert.setTitle("DELETE WARNING");
             alert.setContentText(String.format("Are you sure to delete the item?"));
             Optional<ButtonType> res = alert.showAndWait();


             if (res.isPresent()) {
                 if (res.get().equals(ButtonType.CANCEL))
                     event.consume();

                 if (res.get().equals(ButtonType.YES)) {
                     deleteProduct(table_product.getSelectionModel().getSelectedItem().id);
                     refresh();
                     event.consume();
                     searchName();
                     searchBrand();
                 }
             }
         }
         else {
             Alert alert2 = new Alert(Alert.AlertType.WARNING);
             alert2.setContentText("You are not allowed to delete!");
             alert2.show();
         }
    }


    //Close button's action method
    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) button_exit.getScene().getWindow();
        stage.close();
    }

    //update button's action method
    @FXML
    private void updateAction(ActionEvent event){

        try{

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product-update.fxml"));
            Parent root3 = fxmlLoader.load();
            Node node = (Node) event.getSource();

            Stage stageScene = new Stage();
            Stage stage = (Stage) node.getScene().getWindow();
            boolean isAdmin = (boolean)stage.getUserData();

            if(isAdmin){
                Stage stage2 = new Stage();
                stage2.initStyle(StageStyle.DECORATED);
                stage2.setTitle("Product Update Screen");
                stage2.setScene(new Scene(root3));

                ProductUpdateController controller = fxmlLoader.getController();
                controller.setUpdateDataId(table_product.getSelectionModel().getSelectedItem().id);

                stage2.show();
                searchName();
                searchBrand();
            }
            else{
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setContentText("You are not allowed to update!");
                alert2.show();
            }

        } catch (IOException e) {
            System.out.println("Can't load new window.");
        }
    }


    //show detail button's action method
    @FXML
    private void detailsAction(ActionEvent event){

        try{

            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("product-details.fxml"));
            Parent root3 = fxmlLoader1.load();
            Node node = (Node) event.getSource();

            Stage stageScene = new Stage();
            Stage stage = (Stage) node.getScene().getWindow();
            boolean isAdmin = (boolean)stage.getUserData();

                Stage stage2 = new Stage();
                stage2.initStyle(StageStyle.DECORATED);
                stage2.setTitle("Product Details Screen");
                stage2.setScene(new Scene(root3));

                ProductDetailsController controller = fxmlLoader1.getController();
                controller.setDataId(table_product.getSelectionModel().getSelectedItem().id);

                stage2.show();
                searchName();
                searchBrand();

        } catch (IOException e) {
            System.out.println("Can't load new window.");
        }
    }

    //product stock button's action method
    @FXML
    private void stockAction(ActionEvent event){

        try{
            Connection conn = ProductConnection();
            ObservableList<stock> list = FXCollections.observableArrayList();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product-stock.fxml"));
            Parent root3 = fxmlLoader.load();
            Node node = (Node) event.getSource();

            Stage stageScene = new Stage();
            Stage stage = (Stage) node.getScene().getWindow();
            boolean isAdmin = (boolean)stage.getUserData();

            if(isAdmin){
                Stage stage2 = new Stage();
                stage2.initStyle(StageStyle.DECORATED);
                stage2.setTitle("Product Update Screen");
                stage2.setScene(new Scene(root3));

                stage2.show();

                searchName();
                searchBrand();
            }
            else{
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setContentText("You are not allowed to enter the stock page!");
                alert2.show();
            }

        } catch (IOException e) {
            System.out.println("Can't load new window.");
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<product, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<product, String>("name"));
        col_brand.setCellValueFactory(new PropertyValueFactory<product, String>("brand"));
        col_price.setCellValueFactory(new PropertyValueFactory<product, Integer>("price"));


        listM = DBUtils.getDataProducts();
        table_product.setItems(listM);
        searchName();
        searchBrand();

    }
}
