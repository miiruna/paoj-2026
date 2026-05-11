package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tok = sc.nextLine().trim().split("\\s+");
            Tranzactie t = new Tranzactie(
                    Integer.parseInt(tok[0]),
                    Double.parseDouble(tok[1]),
                    tok[2], tok[3], tok[4],
                    TipTranzactie.valueOf(tok[5]));
            t.setNote("procesat");
            lista.add(t);
        }

        new File("output").mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(lista);
        }

        List<Tranzactie> deserializate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            deserializate = (List<Tranzactie>) ois.readObject();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] tok = line.split("\\s+");
            switch (tok[0]) {
                case "LIST": {
                    deserializate.forEach(System.out::println);
                    break;
                }
                case "FILTER": {
                    String prefix = tok[1];
                    List<Tranzactie> filtrate = new ArrayList<>();
                    for (Tranzactie t : deserializate) {
                        if (t.getData().startsWith(prefix)) filtrate.add(t);
                    }
                    if (filtrate.isEmpty()) {
                        System.out.println("Niciun rezultat.");
                    } else {
                        filtrate.forEach(System.out::println);
                    }
                    break;
                }
                case "NOTE": {
                    int id = Integer.parseInt(tok[1]);
                    Tranzactie gasita = null;
                    for (Tranzactie t : deserializate) {
                        if (t.getId() == id) { gasita = t; break; }
                    }
                    if (gasita == null) {
                        System.out.println("NOTE[" + id + "]: not found");
                    } else {
                        System.out.println("NOTE[" + id + "]: " + gasita.getNote());
                    }
                    break;
                }
            }
        }
    }
}
