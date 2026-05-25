package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
        this.autentificat = false;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("User și parola nu pot fi null sau goale.");
        }
        autentificat = true;
        System.out.println("Inginer " + nume + " " + prenume + " autentificat cu succes.");
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
        return "Inginer{" + nume + " " + prenume + ", salariu=" + salariu + ", sold=" + sold + "}";
    }
}
