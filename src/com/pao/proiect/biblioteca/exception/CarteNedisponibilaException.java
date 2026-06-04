package com.pao.proiect.biblioteca.exception;

public class CarteNedisponibilaException extends Exception {

    public CarteNedisponibilaException(String titluCarte) {
        super("Cartea \"" + titluCarte + "\" nu are exemplare disponibile în acest moment.");
    }

    public CarteNedisponibilaException(String titluCarte, String detalii) {
        super("Cartea \"" + titluCarte + "\" nu este disponibilă: " + detalii);
    }
}
