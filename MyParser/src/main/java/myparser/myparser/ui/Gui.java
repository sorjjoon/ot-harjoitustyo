/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

/**
 *
 * @author joona
 */
    
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


//This is my graphical ui base
 //TODO figure out how this works...
public class Gui extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
//        URL
        System.out.println(getClass().getResource("/xml/MyParserGui.fxml"));
       Parent root = FXMLLoader.load(getClass().getResource("/xml/MyParserGui.fxml"));
        primaryStage.setTitle("Registration Form FXML Application");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
    
    
    public void test(){
        System.out.println("test");
    }
}
    

