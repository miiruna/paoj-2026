package com.pao.laboratory06.exercise3;

public class Angajat extends Persoana {
    protected double salariu;

    public Angajat(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon);
        this.salariu = salariu;
    }

    public double getSalariu() { return salariu; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
}
