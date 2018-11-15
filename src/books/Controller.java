package books;

import books.models.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField author;
    @FXML
    private CheckBox amazon;
    @FXML
    private CheckBox fnac;
    @FXML
    private Button search;
    @FXML
    private Button clear;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private Label alert;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        search.setOnAction(event -> {
            if(author.getText().equals("") && title.getText().equals("")) {
                alert.setVisible(true);
            } else {
                tableView.getItems().clear();
                alert.setVisible(false);
                BookScrapper scrapper = new BookScrapper();
                String t = title.getText();
                String a = author.getText();
                if (amazon.isSelected())
                    tableView.getItems().addAll(scrapper.searchAmazon(t, a));

                if (fnac.isSelected())
                    tableView.getItems().addAll(scrapper.searchFnac(t,a));

                tableView.setVisible(true);
            }
        });
    }
}