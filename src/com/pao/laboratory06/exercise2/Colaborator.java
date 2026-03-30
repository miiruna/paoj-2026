package com.pao.laboratory06.exercise2;

import java.util.Locale;
import java.util.Scanner;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract double calculeazaVenitNetAnual();

    @Override
    public void afiseaza() {
        System.out.printf(Locale.US, "%s: %s %s, venit net anual: %.2f lei%n",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }
}
