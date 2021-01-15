BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "korisnik" (
	"id"	INTEGER,
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	"slika" TEXT,
	PRIMARY KEY("id")
);
    INSERT INTO korisnik(ime,prezime,email,username,password,slika) VALUES("Vedran", "Ljubović", "vljubovic@etf.unsa.ba", "vedranlj", "test","/img/face-smile.png");
    INSERT INTO korisnik(ime,prezime,email,username,password,slika) VALUES("Amra", "Delić", "adelic@etf.unsa.ba", "amrad", "test","/img/face-smile.png");
    INSERT INTO korisnik(ime,prezime,email,username,password,slika) VALUES("Tarik", "Sijerčić", "tsijercic1@etf.unsa.ba", "tariks", "test","/img/face-smile.png");
    INSERT INTO korisnik(ime,prezime,email,username,password,slika) VALUES("Rijad", "Fejzić", "rfejzic1@etf.unsa.ba", "rijadf", "test","/img/face-smile.png");
COMMIT;
