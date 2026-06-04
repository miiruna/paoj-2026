package com.pao.proiect.biblioteca.exception;

public class CititorNegasitException extends Exception {

    public CititorNegasitException(int idCititor) {
        super("Cititorul cu id-ul " + idCititor + " nu a fost găsit în sistem.");
    }

    public CititorNegasitException(String mesaj) {
        super(mesaj);
    }
}
