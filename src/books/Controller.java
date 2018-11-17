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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Falta información por rellenar");

        search.setOnAction(event -> {
            if((!author.getText().isEmpty() || !title.getText().isEmpty())
                && (amazon.isSelected() || fnac.isSelected())) {

                tableView.getItems().clear();
                String t = title.getText();
                String a = author.getText();
                AmazonScrapper amazonScrapper = new AmazonScrapper();
                FnacScrapper fnacScrapper = new FnacScrapper();
                if (amazon.isSelected())
                    tableView.getItems().addAll(amazonScrapper.search(t, a));

                if (fnac.isSelected())
                    tableView.getItems().addAll(fnacScrapper.search(t, a));


                tableView.setVisible(true);
                clear.setVisible(true);
            } else if (author.getText().isEmpty()&& title.getText().isEmpty()
                && !amazon.isSelected() && !fnac.isSelected()) {
                alert.setContentText("Rellene el campo título y/o autor, además seleccione " +
                        "al menos una página de búsqueda");
                alert.showAndWait();
            } else if (author.getText().isEmpty() && title.getText().isEmpty()) {
                alert.setContentText("Rellene el campo título y/ autor");
                alert.showAndWait();
            } else {
                alert.setContentText("Seleccione al menos una página de búsqueda");
                alert.showAndWait();
            }
        });

        clear.setOnAction(event -> {
            tableView.getItems().clear();
            clear.setVisible(false);

        });
    }
}