package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        String bonusStr = in.next();
        this.bonus = bonusStr.equalsIgnoreCase("DA");
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (bonus) net *= 1.10;
        return net;
    }
}
