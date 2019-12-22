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
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Start method for gui launch
 *
 * @author joona
 */
public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/xml/MyParserGui.fxml"));
            primaryStage.setTitle("MyParser");
            primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("There appears to be a problem finding the javafx class, did you use java 8?");
            System.out.println(e.getMessage());
        } catch (Exception e) {

            try {
                new Alert(Alert.AlertType.ERROR, "Unexpected error occured during runtime"+"\n" + e.getMessage()).show();
                System.exit(1);
            } catch (Exception x) { //if for some reason there is an error displaying the pop up
                System.out.println("Unexpected error occured during runtime");
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

    }

}
