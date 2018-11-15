package books;

import books.models.Book;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("books.fxml"));
        primaryStage.setTitle("Búsqueda de libros");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(900);
        primaryStage.show();
    }


    public static void main(String[] args) {
        AmazonScrapper scrapper = new AmazonScrapper();
        List<Book> books = scrapper.search("guerra mundial", "");
        launch(args);
        BookScrapper scrapper = new BookScrapper();


    }
}
