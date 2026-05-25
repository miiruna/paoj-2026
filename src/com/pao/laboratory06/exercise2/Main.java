package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> {
                    CIMColaborator obj = new CIMColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "PFA" -> {
                    PFAColaborator obj = new PFAColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "SRL" -> {
                    SRLColaborator obj = new SRLColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaboratori.add(c);
        }
        // Sortează și afișează pe tip, fiecare descrescător după venit net anual
        for (TipColaborator tipColab : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(c -> c.getTip() == tipColab)
                    .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                    .forEach(Colaborator::afiseaza);
        }
        // Colaborator cu venit net maxim
        Colaborator max = colaboratori.stream().max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual)).orElse(null);
        System.out.printf("%nColaborator cu venit net maxim: ");
        if (max != null) max.afiseaza();
        // Colaboratori persoane juridice (SRL)
        System.out.printf("%nColaboratori persoane juridice:%n");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                .forEach(Colaborator::afiseaza);
        // Sume și număr colaboratori pe tip
        System.out.printf("%nSume și număr colaboratori pe tip:%n");
        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
        for (Colaborator c : colaboratori) {
            TipColaborator t = c.getTip();
            suma.merge(t, c.calculeazaVenitNetAnual(), Double::sum);
            numar.merge(t, 1, Integer::sum);
        }
        for (TipColaborator t : TipColaborator.values()) {
            Double s = suma.get(t);
            Integer nr = numar.get(t);
            String sumaStr = s != null ? String.format(Locale.US, "%.2f", s) : "nu";
            System.out.printf("%s: suma = %s lei, număr = %s%n", t, sumaStr, nr);
        }
    }
}