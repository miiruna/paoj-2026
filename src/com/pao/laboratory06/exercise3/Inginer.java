package com.pao.laboratory06.exercise3;

import java.util.Locale;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;

    public Inginer(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon, salariu);
        this.sold = salariu;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("User sau parola nu poate fi null/gol");
        }
        System.out.println("Inginer autentificat: " + user);
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (sold >= suma) {
            sold -= suma;
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Inginer other) {
        return this.nume.compareTo(other.nume);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Inginer{%s %s, salariu=%.2f}", nume, prenume, salariu);
    }
}
