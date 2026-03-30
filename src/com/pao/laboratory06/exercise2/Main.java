package com.pao.laboratory06.exercise2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Colaborator[] colaboratori = new Colaborator[n];
        for (int i = 0; i < n; i++) {
            String tip = sc.next();
            Colaborator c = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            c.citeste(sc);
            colaboratori[i] = c;
        }

        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }

        Colaborator maxColaborator = Arrays.stream(colaboratori)
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        System.out.println();
        System.out.print("Colaborator cu venit net maxim: ");
        if (maxColaborator != null) maxColaborator.afiseaza();

        System.out.println();
        System.out.println("Colaboratori persoane juridice:");
        for (Colaborator c : colaboratori) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }

        System.out.println();
        System.out.println("Sume \u0219i num\u0103r colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            String tipStr = tip.name();
            double suma = 0;
            int numar = 0;
            for (Colaborator c : colaboratori) {
                if (c.tipContract().equals(tipStr)) {
                    suma += c.calculeazaVenitNetAnual();
                    numar++;
                }
            }
            if (numar > 0) {
                System.out.printf(Locale.US, "%s: suma = %.2f lei, num\u0103r = %d%n", tipStr, suma, numar);
            }
        }
    }
}
