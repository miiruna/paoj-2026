package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieliLunare;

    private static final double SALARIU_MINIM = 4050.0;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNet;

        double cass;
        if (venitNet < 6 * SALARIU_MINIM) {
            cass = 0.10 * 6 * SALARIU_MINIM;
        } else if (venitNet <= 72 * SALARIU_MINIM) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * 72 * SALARIU_MINIM;
        }

        double cas;
        if (venitNet < 12 * SALARIU_MINIM) {
            cas = 0;
        } else if (venitNet <= 24 * SALARIU_MINIM) {
            cas = 0.25 * 12 * SALARIU_MINIM;
        } else {
            cas = 0.25 * 24 * SALARIU_MINIM;
        }

        return venitNet - impozit - cass - cas;
    }
}
