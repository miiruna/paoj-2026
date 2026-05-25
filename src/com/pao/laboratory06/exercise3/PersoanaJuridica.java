package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    private List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("User și parola nu pot fi null sau goale.");
        }
        System.out.println("PersoanaJuridica " + nume + " autentificată cu succes.");
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
        if (mesaj == null || mesaj.isBlank()) return false;
        if (telefon == null || telefon.isBlank()) return false;
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }
}
