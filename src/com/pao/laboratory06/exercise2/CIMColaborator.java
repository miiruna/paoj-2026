package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        if (in.hasNext("[A-Z]+")) {
            String bonusStr = in.next();
            bonus = bonusStr.equalsIgnoreCase("DA");
        } else {
            bonus = false;
        }
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            net *= 1.10;
        }
        return net;
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }
}
