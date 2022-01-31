package com.example.javafxlogin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.DriverManager;


import java.sql.*;

import java.io.IOException;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, Boolean isAdmin) {

        Parent root = null;

        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Node node = (Node) event.getSource();


        Stage stage = (Stage) node.getScene().getWindow();

        stage.setUserData(isAdmin);
        stage.setTitle(title);
        stage.setScene(new Scene(root, 1015, 606));
        stage.show();
    }




    public static void signUpUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-app", "root", "Be3506562@");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User is already exists!!!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use the username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();


                changeScene(event, "logged-in.fxml", "Welcome!", username,null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;

        ResultSet resultSet = null;
        ResultSet resultSet1 = null;


        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-app", "root", "Be3506562@");
            preparedStatement = connection.prepareStatement("SELECT u.user_id,u.username,password,ur.role_id, r.role_name FROM users as u left join user_roles as ur on u.user_id = ur.user_id left join roles as r on r.role_id = ur.role_id where u.username = ? AND u.password = ? ");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {

                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {
                        int userId = resultSet.getInt("user_id");
                        preparedStatement2 = connection.prepareStatement("SELECT role_id FROM user_roles WHERE user_id = ?");
                        preparedStatement2.setInt(1, userId);
                        resultSet1 = preparedStatement2.executeQuery();

                        LoggedInController lc = null;
                        boolean isAdmin = resultSet.getInt("role_id") == 1 ? true : false;

                        if (resultSet.getInt("role_id") == 1) {
                            lc = new LoggedInController(true);
                            changeScene(event, "logged-in.fxml", "Welcome!", username, true);
                        } else {
                            lc = new LoggedInController(false);
                            changeScene(event, "logged-in.fxml", "Welcome!", username, false);
                        }
                    }
                     else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect!");
                        alert.show();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void changePassword(ActionEvent event, String username, String password, String newPassword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-app", "root", "Be3506562@");
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();

            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {

                        preparedStatement.executeUpdate("UPDATE users SET password = '" +newPassword +"' WHERE username ='" +username +"' AND password = '" +password +"'");
                        resultSet = preparedStatement.executeQuery();


                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Your password is changed!!!");
                        alert.show();


                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect!");
                        alert.show();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    static Connection conn = null;

    public static Connection ProductConnection() {

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-app", "root", "Be3506562@");
            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<product> getDataProducts(){

        Connection conn = ProductConnection();
        ObservableList<product> list = FXCollections.observableArrayList();


        try{
            PreparedStatement ps = conn.prepareStatement("Select * from product");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                product pr = new product();
                pr.setId(Integer.parseInt(rs.getString("product_id")));
                pr.setName(rs.getString("product_name"));
                pr.setBrand(rs.getString("product_brand"));
                pr.setPrice(Integer.parseInt(rs.getString("product_price")));

                list.add(pr);
            }



        } catch(Exception e){

        }

        return  list;
    }

    public static ObservableList<product> getDataProductsById(int id){

        Connection conn = ProductConnection();
        ObservableList<product> list = FXCollections.observableArrayList();


        try{
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE product_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                product pr = new product();
                pr.setId(Integer.parseInt(rs.getString("product_id")));
                pr.setName(rs.getString("product_name"));
                pr.setBrand(rs.getString("product_brand"));
                pr.setPrice(Integer.parseInt(rs.getString("product_price")));
                list.add(pr);
            }

        } catch(Exception e){

        }

        return  list;
    }

    public static ObservableList<stock> getDataStockbyId(int id){

        Connection conn = ProductConnection();
        ObservableList<stock> list = FXCollections.observableArrayList();


        try{
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM stock WHERE stock_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                stock s = new stock();
                s.setStock_id(Integer.parseInt(rs.getString("stock_id")));
                s.setQuantity(Integer.parseInt(rs.getString("quantity")));

                list.add(s);
            }

        } catch(Exception e){

        }

        return  list;
    }

    public static ObservableList<stock> getDataStock(){

        Connection conn = ProductConnection();
        ObservableList<stock> list = FXCollections.observableArrayList();


        try{
            PreparedStatement ps = conn.prepareStatement("select s.stock_id, p.product_name, p.product_brand, p.product_price, s.quantity, s.location from product p inner join stock s on p.product_id = s.product_id");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                stock s = new stock();
                s.setStock_id(Integer.parseInt(rs.getString("stock_id")));
                s.setProduct_name(rs.getString("product_name"));
                s.setProduct_brand(rs.getString("product_brand"));
                s.setProduct_price(Integer.parseInt(rs.getString("product_price")));
                s.setQuantity(Integer.parseInt(rs.getString("quantity")));
                s.setLocation(rs.getString("location"));

                list.add(s);
            }



        } catch(Exception e){

        }

        return  list;
    }



    public static void deleteProduct(int id){

        Connection conn = ProductConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement("DELETE FROM product WHERE product_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }






}
