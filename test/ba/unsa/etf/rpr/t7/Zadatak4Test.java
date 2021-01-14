package ba.unsa.etf.rpr.t7;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Zadatak4Test {

    @Test
    void testZapisiDatoteku() {
        // Default podaci
        KorisniciModel model = new KorisniciModel();
        model.vratiNaDefault();
        model.napuni();

        File file = new File("test.txt");
        model.zapisiDatoteku(file);

        try {
            Scanner scanner = new Scanner(file);
            assertEquals("vedranlj:test:1:1:Vedran Ljubović::", scanner.nextLine().trim());
            assertEquals("amrad:test:2:2:Amra Delić::", scanner.nextLine().trim());
            assertEquals("tariks:test:3:3:Tarik Sijerčić::", scanner.nextLine().trim());
            assertEquals("rijadf:test:4:4:Rijad Fejzić::", scanner.nextLine().trim());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testNemojZapisatiDatoteku() {
        // Default podaci
        KorisniciModel model = new KorisniciModel();
        model.vratiNaDefault();
        model.napuni();

        // Metoda ne smije da se krahira ako se pošalje null datoteka
        // (tj. ako je korisnik na dijalogu kliknuo Cancel)
        model.zapisiDatoteku(null);
    }
}
