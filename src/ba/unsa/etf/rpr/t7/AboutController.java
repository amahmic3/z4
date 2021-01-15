package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class AboutController {
    public ImageView slika;
    Image nova;
    @FXML
    public void initialize(){
    slika.setImage(nova);
    }
    public AboutController(){
            nova=new Image("/img/slika.jpg");
    }
    public void zatvori(ActionEvent actionEvent){
        ((Stage)(((Node)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
