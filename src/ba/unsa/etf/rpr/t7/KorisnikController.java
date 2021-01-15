package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class KorisnikController {
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldEmail;
    public TextField fldUsername;
    public ListView<Korisnik> listKorisnici;
    public PasswordField fldPassword;
    public Button imgKorisnik;

    private KorisniciModel model;

    public KorisnikController(KorisniciModel model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        listKorisnici.setItems(model.getKorisnici());
        listKorisnici.getSelectionModel().selectedItemProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            model.setTrenutniKorisnik(newKorisnik);
            listKorisnici.refresh();
         });
        model.trenutniKorisnikProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            if (oldKorisnik != null) {
                fldIme.textProperty().unbindBidirectional(oldKorisnik.imeProperty() );
                fldPrezime.textProperty().unbindBidirectional(oldKorisnik.prezimeProperty() );
                fldEmail.textProperty().unbindBidirectional(oldKorisnik.emailProperty() );
                fldUsername.textProperty().unbindBidirectional(oldKorisnik.usernameProperty() );
                fldPassword.textProperty().unbindBidirectional(oldKorisnik.passwordProperty() );
            }
            if (newKorisnik == null) {
                fldIme.setText("");
                fldPrezime.setText("");
                fldEmail.setText("");
                fldUsername.setText("");
                fldPassword.setText("");

            }
            else {
                fldIme.textProperty().bindBidirectional( newKorisnik.imeProperty() );
                fldPrezime.textProperty().bindBidirectional( newKorisnik.prezimeProperty() );
                fldEmail.textProperty().bindBidirectional( newKorisnik.emailProperty() );
                fldUsername.textProperty().bindBidirectional( newKorisnik.usernameProperty() );
                fldPassword.textProperty().bindBidirectional( newKorisnik.passwordProperty() );
                Thread promjenaSlike = new Thread(()->{
                    Image slika = new Image(newKorisnik.getSlika(),128,128,false,false,true);
                    if(slika.isError()) System.out.println(slika.getException());
                    imgKorisnik.setGraphic(new ImageView(slika));
                });
                promjenaSlike.run();
            }

        });

        fldIme.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldIme.getStyleClass().removeAll("poljeNijeIspravno");
                fldIme.getStyleClass().add("poljeIspravno");
            } else {
                fldIme.getStyleClass().removeAll("poljeIspravno");
                fldIme.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPrezime.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPrezime.getStyleClass().removeAll("poljeNijeIspravno");
                fldPrezime.getStyleClass().add("poljeIspravno");
            } else {
                fldPrezime.getStyleClass().removeAll("poljeIspravno");
                fldPrezime.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldEmail.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldEmail.getStyleClass().removeAll("poljeNijeIspravno");
                fldEmail.getStyleClass().add("poljeIspravno");
            } else {
                fldEmail.getStyleClass().removeAll("poljeIspravno");
                fldEmail.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldUsername.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
                fldUsername.getStyleClass().add("poljeIspravno");
            } else {
                fldUsername.getStyleClass().removeAll("poljeIspravno");
                fldUsername.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPassword.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldPassword.getStyleClass().removeAll("poljeIspravno");
                fldPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });
    }

    public void dodajAction(ActionEvent actionEvent) {
        model.getKorisnici().add(new Korisnik("", "", "", "", ""));
        listKorisnici.getSelectionModel().selectLast();
    }

    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }
    public void obrisiAction(ActionEvent actionEvent){
        if(model.getTrenutniKorisnik()!=null) {
            Korisnik obrisani = model.getTrenutniKorisnik();
            listKorisnici.getSelectionModel().select(null);
            model.getKorisnici().removeAll(obrisani);
            listKorisnici.refresh();
            Platform.runLater(() -> {
                model.obrisiKorisnika(obrisani.getId());
            });
        }
    }
    public void abouUsAction(ActionEvent actionEvent) throws IOException {
        Stage prozor = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
        loader.setController(new AboutController());
        prozor.setResizable(false);
        prozor.setTitle("About");
        prozor.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,USE_COMPUTED_SIZE));
        prozor.show();
    }
    public void spasi(ActionEvent actionEvent){
        MenuItem menuItem = (MenuItem)actionEvent.getTarget();
        ContextMenu cm = menuItem.getParentPopup();
        Scene scene = cm.getScene();
        Window window = scene.getWindow();

        FileChooser fajl = new FileChooser();
        fajl.setTitle("Save");
        fajl.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files","*.txt"));
        model.zapisiDatoteku(fajl.showSaveDialog(window));
    }
    public void pretragaSlike(ActionEvent actionEvent) throws IOException {
        Stage pretraga = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pretragaSlike.fxml"));
        loader.setController(new PretragaController());
        pretraga.setTitle("Pretraga slike");
        pretraga.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,400));
        pretraga.show();
        pretraga.setOnHiding((e)->{
            PretragaController kontroler = loader.getController();
            if(kontroler.isPromjeniSliku()){
                model.getTrenutniKorisnik().setSlika(kontroler.getUrlSlike());
                Platform.runLater(()->{
                    Image slika = new Image(model.getTrenutniKorisnik().getSlika(),128,128,false,false,true);
                    if(slika.isError()) System.out.println(slika.getException());
                    imgKorisnik.setGraphic(new ImageView(slika));
                });
            }
        });
    }
}
