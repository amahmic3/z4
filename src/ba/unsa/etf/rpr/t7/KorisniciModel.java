package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;

public class KorisniciModel {
    private ObservableList<Korisnik> korisnici = FXCollections.observableArrayList();
    private SimpleObjectProperty<Korisnik> trenutniKorisnik = new SimpleObjectProperty<>();
    private Connection konekcija;
    private PreparedStatement upitZaPunjenje,upitZaPromjenu,upitZaBrisanje;

    public KorisniciModel() {
        try {
            konekcija= DriverManager.getConnection("jdbc:sqlite:korisnici.db");
            upitZaPunjenje=konekcija.prepareStatement("SELECT id,ime,prezime,email,username,password,slika FROM korisnik;");
            upitZaPromjenu=konekcija.prepareStatement("UPDATE korisnik SET ime=?,prezime=?,email=?,username=?,password=?,slika=? WHERE id=?;");
            upitZaBrisanje=konekcija.prepareStatement("DELETE FROM korisnik WHERE id=?;");
            //vratiNaDefault();
        } catch (SQLException throwables) {
            regenerisiBazu();
            try {
                upitZaPunjenje=konekcija.prepareStatement("SELECT id,ime,prezime,email,username,password,slika FROM korisnik;");
                upitZaPromjenu=konekcija.prepareStatement("UPDATE korisnik SET ime=?,prezime=?,email=?,username=?,password=? WHERE id=?;");
                upitZaBrisanje=konekcija.prepareStatement("DELETE FROM korisnik WHERE id=?;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void regenerisiBazu(){
        try {
            Scanner citac = new Scanner(new FileInputStream("korisnici.sql"));
            StringBuilder upit=new StringBuilder("");
            while(citac.hasNextLine()){
                upit.append(citac.nextLine());
                if(upit.toString().length()>0 && upit.toString().charAt(upit.toString().length()-1)==';'){
                    konekcija.createStatement().executeUpdate(upit.toString());
                    upit.setLength(0);
                }
            }
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void napuni() {
        // Ako je lista već bila napunjena, praznimo je
        // Na taj način se metoda napuni() može pozivati više puta u testovima
        korisnici.clear();
        try {
            ResultSet rezultatUpita = upitZaPunjenje.executeQuery();
            while(rezultatUpita.next()){
                korisnici.add(new Korisnik(rezultatUpita.getInt(1),rezultatUpita.getString(2),rezultatUpita.getString(3),
                        rezultatUpita.getString(4), rezultatUpita.getString(5),
                        rezultatUpita.getString(6),rezultatUpita.getString(7)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        trenutniKorisnik.set(null);
    }

    public void vratiNaDefault() {
        // Dodali smo metodu vratiNaDefault koja trenutno ne radi ništa, a kada prebacite Model na DAO onda
        // implementirajte ovu metodu
        // Razlog za ovo je da polazni testovi ne bi padali nakon što dodate bazu
        try {
            konekcija.createStatement().executeUpdate("DELETE FROM korisnik;");
            regenerisiBazu();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void diskonektuj() {
        if(konekcija!=null){
        try {
            konekcija.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        }
        konekcija=null;
    }

    public ObservableList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(ObservableList<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public Korisnik getTrenutniKorisnik() {
        return trenutniKorisnik.get();
    }

    public SimpleObjectProperty<Korisnik> trenutniKorisnikProperty() {
        return trenutniKorisnik;
    }

    public void setTrenutniKorisnik(Korisnik trenutniKorisnik) {
        if(this.trenutniKorisnik.get()!=null){
            Korisnik k=this.trenutniKorisnik.get();
             Thread azuriranje = new Thread(()->{azururajKorisnika(k);});
             azuriranje.run();
        }
        this.trenutniKorisnik.set(trenutniKorisnik);
    }

    public void setTrenutniKorisnik(int i) {
        this.trenutniKorisnik.set(korisnici.get(i));
    }
    public void azururajKorisnika(Korisnik k){
        if(k.getId()!=0) {
            try {
                upitZaPromjenu.setString(1, k.getIme());
                upitZaPromjenu.setString(2, k.getPrezime());
                upitZaPromjenu.setString(3, k.getEmail());
                upitZaPromjenu.setString(4, k.getUsername());
                upitZaPromjenu.setString(5, k.getPassword());
                upitZaPromjenu.setString(6, k.getSlika());
                upitZaPromjenu.setInt(7,k.getId());
                upitZaPromjenu.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{

        }
    }
    public void obrisiKorisnika(int k){
        try {
            upitZaBrisanje.setInt(1,k);
            upitZaBrisanje.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void zapisiDatoteku(File fajl){
        if(fajl!=null){
            try {
                PrintWriter pisac = new PrintWriter(fajl);
                for(Korisnik k: this.getKorisnici()){
                    pisac.println(k.getUsername()+":"+k.getPassword()+":"+k.getId()+":"+k.getId()+":"+k.getIme()+" "+k.getPrezime()+"::");
                }
                pisac.flush();
                pisac.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
