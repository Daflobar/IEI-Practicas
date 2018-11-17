package books;

import books.models.Book;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("books.fxml"));
        primaryStage.setTitle("Búsqueda de libros");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(900);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("open-book.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);


    }
}
