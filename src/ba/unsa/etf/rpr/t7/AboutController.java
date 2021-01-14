package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AboutController {
    @FXML
    public void initialize(){

    }
    public AboutController(){

    }
    public void zatvori(ActionEvent actionEvent){
        ((Stage)(((Node)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
