package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class PretragaController {
    private SimpleStringProperty urlSlike=new SimpleStringProperty("");
    private final String urlPretrage ="https://api.giphy.com/v1/gifs/search?api_key=dc6zaTOxFJmzC&q=";
    private JSONObject jsonObjectn;
    public TextField fldTekst;
    private Button[] tipke = new Button[25];
    private String[] linkovi = new String[25];
    private boolean promjeniSliku=false;
    private String džejson;
    private int brojSlika;
    @FXML
    Parent root;
    public PretragaController(){
        brojSlika=0;
    }
    @FXML
    public void initialize(){
        for (int i = 0; i < 25; i++) {
            tipke[i] = (Button) root.lookup("#slika" + i);
        }
    }

    public void pretraga(ActionEvent actionEvent){
        if(!fldTekst.getText().equals("")) {
            Task<Void> posao = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    URL konekcija = new URL(new String(urlPretrage + fldTekst.getText()+"&limit=25").replace(" ","%20"));
                    InputStream stream = konekcija.openStream();
                    džejson=new String(stream.readAllBytes());
                    try {
                        jsonObjectn = new JSONObject(džejson);
                        JSONArray gifovi = jsonObjectn.getJSONArray("data");

                        for (int i = 0; i < gifovi.length(); i++) {
                            int finalI = i;
                            Platform.runLater(()-> {
                                        tipke[finalI].setGraphic(new ImageView(new Image("/img/loading.gif", 128, 128, false, false)));
                                        tipke[finalI].visibleProperty().setValue(true);
                                    });
                            URL link = new URL(gifovi.getJSONObject(i).getJSONObject("images").getJSONObject("original_still").getString("url"));
                            linkovi[i] = "https://i.giphy.com" + link.getPath();
                            System.out.println(linkovi[i]);
                            Thread.sleep(400);
                            int finalI1 = i;
                            Platform.runLater(()-> {
                                Image novaSlika = new Image(linkovi[finalI1], 128, 128, false, false, true);
                                tipke[finalI1].setGraphic(new ImageView(novaSlika));
                            });
                            }
                        System.out.println(brojSlika+ " "+ gifovi.length());
                        if(brojSlika>gifovi.length()){
                            for(int i=gifovi.length();i<brojSlika;i++) tipke[i].setVisible(false);
                            brojSlika=gifovi.length();
                        }else if(gifovi.length()>=25){
                            brojSlika=25;
                        }else brojSlika=gifovi.length();
                    } catch (MalformedURLException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            new Thread(posao).start();
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
    public void promjeniSliku(ActionEvent actionEvent) {
        if (!urlSlike.get().equals("")) {
            promjeniSliku = true;
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        }else{
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("naslovAlerta"));
            alert.setHeaderText(bundle.getString("headerAlerta"));
            alert.setContentText(bundle.getString("tekstAlerta"));
            alert.showAndWait();
        }
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
