package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


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
    public void otvoriLink(ActionEvent actionEvent) {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI("https://github.com/RPR-2019/rpr20-zadaca4-amahmic3"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }
}
