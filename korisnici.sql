BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "korisnik" (
	"id"	INTEGER,
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	PRIMARY KEY("id")
);
    INSERT INTO korisnik(ime,prezime,email,username,password) VALUES("Vedran", "Ljubović", "vljubovic@etf.unsa.ba", "vedranlj", "test");
    INSERT INTO korisnik(ime,prezime,email,username,password) VALUES("Amra", "Delić", "adelic@etf.unsa.ba", "amrad", "test");
    INSERT INTO korisnik(ime,prezime,email,username,password) VALUES("Tarik", "Sijerčić", "tsijercic1@etf.unsa.ba", "tariks", "test");
    INSERT INTO korisnik(ime,prezime,email,username,password) VALUES("Rijad", "Fejzić", "rfejzic1@etf.unsa.ba", "rijadf", "test");
COMMIT;
