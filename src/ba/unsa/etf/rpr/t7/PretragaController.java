package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class PretragaController {
    private SimpleStringProperty urlSlike=new SimpleStringProperty("");
    private final String urlPretrage ="https://api.giphy.com/v1/gifs/search?api_key=dc6zaTOxFJmzC&q=";
    private JSONObject jsonObjectn;
    public TextField fldTekst;
    private Button[] tipke = new Button[25];
    private String[] linkovi = new String[25];
    private boolean promjeniSliku=false;
    public PretragaController(){

    }
    @FXML
    public void initialize(){

    }
    public void pretraga(ActionEvent actionEvent){
        if(!fldTekst.getText().equals("")) {

            for (int i = 0; i < 25; i++) {
                tipke[i] = (Button) ((Node) actionEvent.getSource()).getScene().lookup("#slika" + i);
            }
            try {
                URL konekcija = new URL(urlPretrage + fldTekst.getText());
                InputStream stream = konekcija.openStream();
                jsonObjectn = new JSONObject(new String(stream.readAllBytes()));
                JSONArray gifovi = jsonObjectn.getJSONArray("data");
                for (int i = 0; i<25 && i < gifovi.length(); i++) {

                    tipke[i].setGraphic(new ImageView(new Image("/img/loading.gif",128,128,false,false)));
                    tipke[i].visibleProperty().setValue(true);

                    URL link = new URL(gifovi.getJSONObject(i).getJSONObject("images").getJSONObject("original_still").getString("url"));
                    linkovi[i]="https://i.giphy.com" + link.getPath();
                    System.out.println(linkovi[i]);
                    int finalI = i;
                    Platform.runLater(()-> {
                        tipke[finalI].visibleProperty().setValue(true);
                        Image novaSlika = new Image(linkovi[finalI], 128, 128, false, false);
                        tipke[finalI].setGraphic(new ImageView(novaSlika));
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void izlaz(ActionEvent actionEvent){
        promjeniSliku=false;
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }
    private int odrediId(String id){
        char zadnji=id.charAt(id.length()-1),predzadnji=id.charAt(id.length()-2);
        if(predzadnji<'0' || predzadnji>'9') return zadnji-'0';
        return (predzadnji-'0')*10+(zadnji-'0');
    }
    public void odaberi(ActionEvent actionEvent){
        Button tipka = (Button)actionEvent.getSource();
        urlSlike.set(linkovi[odrediId(tipka.getId())]);
        System.out.println(urlSlike.get());
    }

    public boolean isPromjeniSliku() {
        return promjeniSliku;
    }
    public void promjeniSliku(ActionEvent actionEvent){
        promjeniSliku=true;
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    public String getUrlSlike() {
        return urlSlike.get();
    }

    public SimpleStringProperty urlSlikeProperty() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike.set(urlSlike);
    }
}
