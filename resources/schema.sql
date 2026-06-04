DROP TABLE IF EXISTS imprumuturi;
DROP TABLE IF EXISTS carti;
DROP TABLE IF EXISTS cititori;
DROP TABLE IF EXISTS autori;
DROP TABLE IF EXISTS sectiuni;

CREATE TABLE sectiuni (
    nume VARCHAR(100) PRIMARY KEY,
    descriere VARCHAR(255) NOT NULL
);

CREATE TABLE autori (
    email VARCHAR(150) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    nationalitate VARCHAR(100) NOT NULL
);

CREATE TABLE cititori (
    id INT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE carti (
    isbn VARCHAR(20) PRIMARY KEY,
    titlu VARCHAR(200) NOT NULL,
    autor_email VARCHAR(150) NOT NULL,
    sectiune_nume VARCHAR(100) NOT NULL,
    numar_total_exemplare INT NOT NULL,
    exemplare_disponibile INT NOT NULL,
    numar_total_imprumuturi INT NOT NULL,
    CONSTRAINT fk_carti_autori FOREIGN KEY (autor_email) REFERENCES autori(email),
    CONSTRAINT fk_carti_sectiuni FOREIGN KEY (sectiune_nume) REFERENCES sectiuni(nume)
);

CREATE TABLE imprumuturi (
    id INT PRIMARY KEY,
    cititor_id INT NOT NULL,
    carte_isbn VARCHAR(20) NOT NULL,
    data_imprumut DATE NOT NULL,
    data_scadenta DATE NOT NULL,
    data_returnare DATE,
    CONSTRAINT fk_imprumuturi_cititori FOREIGN KEY (cititor_id) REFERENCES cititori(id),
    CONSTRAINT fk_imprumuturi_carti FOREIGN KEY (carte_isbn) REFERENCES carti(isbn)
);
