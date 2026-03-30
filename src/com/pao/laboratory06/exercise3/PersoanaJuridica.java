package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private List<String> smsTrimise;
    private double sold;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.smsTrimise = new ArrayList<>();
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("User sau parola nu poate fi null/gol");
        }
        System.out.println("PersoanaJuridica autentificata: " + user);
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
    public boolean trimiteSMS(String mesaj) {
        if (telefon == null || telefon.isEmpty()) return false;
        if (mesaj == null || mesaj.isEmpty()) return false;
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() { return smsTrimise; }

    @Override
    public String toString() {
        return String.format("PersoanaJuridica{%s %s, telefon=%s}", nume, prenume, telefon);
    }
}
