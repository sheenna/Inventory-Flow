package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;


/** This class creates an application that manages products and parts as part of an inventory management program.
 * This file is the Main file that begins the application.
 */
 
public class Main extends Application {

    
    public static void main(String[] args) {

        Inventory.addTestData();
        launch(args);

    }

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
}