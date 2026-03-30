package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 1. Creare si sortare array de Inginer
        Inginer[] ingineri = {
            new Inginer("Zamfir", "Ion", "0700000001", 8000),
            new Inginer("Avram", "Maria", "0700000002", 12000),
            new Inginer("Mihai", "Dan", null, 9500)
        };

        System.out.println("--- Sortare naturala (dupa nume) ---");
        Arrays.sort(ingineri);
        for (Inginer ing : ingineri) System.out.println(ing);

        System.out.println("\n--- Sortare dupa salariu descrescator ---");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer ing : ingineri) System.out.println(ing);

        // 2. Acces prin referinta de tip interfata PlataOnline
        System.out.println("\n--- Acces prin referinta PlataOnline ---");
        PlataOnline contOnline = new Inginer("Ionescu", "Vlad", "0799999999", 7000);
        contOnline.autentificare("vlad_user", "parola123");
        System.out.println("Sold: " + contOnline.consultareSold());
        System.out.println("Plata 1000: " + contOnline.efectuarePlata(1000));
        System.out.println("Sold dupa plata: " + contOnline.consultareSold());

        // 3. PersoanaJuridica prin referinta PlataOnlineSMS
        System.out.println("\n--- PersoanaJuridica prin referinta PlataOnlineSMS ---");
        PlataOnlineSMS firma = new PersoanaJuridica("TechSRL", "SRL", "0722334455", 50000);
        firma.autentificare("techsrl_admin", "pass456");
        System.out.println("Trimitere SMS valid: " + firma.trimiteSMS("Plata confirmata: 500 RON"));
        System.out.println("Trimitere SMS gol: " + firma.trimiteSMS(""));
        System.out.println("SMS trimise: " + ((PersoanaJuridica) firma).getSmsTrimise());

        System.out.println("\n--- PersoanaJuridica fara telefon ---");
        PlataOnlineSMS firmaFaraTelefon = new PersoanaJuridica("MicroSRL", "SRL", null, 10000);
        System.out.println("Trimitere SMS fara telefon: " + firmaFaraTelefon.trimiteSMS("Test"));

        // 4. Constanta din enum
        System.out.println("\n--- Constante financiare ---");
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare());
        System.out.println("Cota impozit: " + ConstanteFinanciare.COTA_IMPOZIT.getValoare());

        // 5. Edge cases - tratare erori
        System.out.println("\n--- Tratare erori ---");
        try {
            contOnline.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare cu user null: " + e.getMessage());
        }
        try {
            contOnline.autentificare("user", "");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare cu parola goala: " + e.getMessage());
        }
    }
}

