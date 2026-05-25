package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // --- Constante financiare ---
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim brut: " + ConstanteFinanciare.SALARIU_MINIM.getValoare());
        System.out.println("Cota impozit: " + ConstanteFinanciare.COTA_IMPOZIT.getValoare());

        // --- Creare ingineri ---
        Inginer[] ingineri = {
            new Inginer("Popescu", "Ion", "0740000001", 8000, 5000),
            new Inginer("Andrei", "Maria", "0740000002", 12000, 10000),
            new Inginer("Constantin", "Dan", "0740000003", 6000, 3000),
        };

        // --- Sortare naturală (după nume, alfabetic) ---
        Arrays.sort(ingineri);
        System.out.println("\nSortare naturala (dupa nume):");
        for (Inginer i : ingineri) System.out.println("  " + i);

        // --- Sortare după salariu descrescător cu ComparatorInginerSalariu ---
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\nSortare dupa salariu descrescator:");
        for (Inginer i : ingineri) System.out.println("  " + i);

        // --- Acces la Inginer prin referința PlataOnline (nu se pot apela metode specifice) ---
        PlataOnline plataInginer = ingineri[0];
        plataInginer.autentificare("user1", "parola1");
        System.out.println("Sold inginer (prin PlataOnline): " + plataInginer.consultareSold());
        System.out.println("Plata 1000 reusita: " + plataInginer.efectuarePlata(1000));

        // --- PersoanaJuridica prin referința PlataOnlineSMS ---
        PersoanaJuridica pj = new PersoanaJuridica("TechSRL", "SRL", "0720000001", 50000);
        PlataOnlineSMS plataSMS = pj;
        plataSMS.autentificare("admin", "secret");
        System.out.println("\nSold PJ (prin PlataOnlineSMS): " + plataSMS.consultareSold());
        System.out.println("Plata 2000 reusita: " + plataSMS.efectuarePlata(2000));

        // --- Trimitere SMS valida ---
        boolean trimis = plataSMS.trimiteSMS("Plata confirmata!");
        System.out.println("SMS trimis: " + trimis);
        System.out.println("SMS-uri stocate: " + pj.getSmsTrimise());

        // --- Trimitere SMS cu mesaj gol ---
        trimis = plataSMS.trimiteSMS("");
        System.out.println("SMS cu mesaj gol trimis: " + trimis);

        // --- PersoanaJuridica fara telefon ---
        PersoanaJuridica pjFaraTel = new PersoanaJuridica("NoPhone", "SRL", null, 10000);
        trimis = pjFaraTel.trimiteSMS("Test");
        System.out.println("SMS catre PJ fara telefon trimis: " + trimis);

        // --- Autentificare cu user null => IllegalArgumentException ---
        try {
            plataInginer.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("\nExceptie autentificare user null: " + e.getMessage());
        }

        // --- Apel trimiteSMS pe un PlataOnline care nu implementeaza SMS => UnsupportedOperationException ---
        // PlataOnline nu are metoda trimiteSMS, deci nu se poate apela direct.
        // Demonstram ca un cast gresit la PlataOnlineSMS pe un Inginer arunca ClassCastException:
        try {
            PlataOnlineSMS smsGresit = (PlataOnlineSMS) plataInginer;
            smsGresit.trimiteSMS("test");
        } catch (ClassCastException e) {
            System.out.println("Exceptie cast gresit (Inginer nu implementeaza PlataOnlineSMS): " + e.getClass().getSimpleName());
        }
    }
}
