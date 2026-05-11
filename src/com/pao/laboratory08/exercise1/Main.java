package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

        List<Student> studenti = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }

        Scanner sc = new Scanner(System.in);
        String comanda = sc.nextLine().trim();
        String[] tok = comanda.split(" ", 2);

        switch (tok[0]) {
            case "PRINT": {
                for (Student s : studenti) System.out.println(s);
                break;
            }
            case "SHALLOW": {
                String nume = tok[1].trim();
                for (Student s : studenti) {
                    if (s.getNume().equals(nume)) {
                        Student clona = s.shallowClone();
                        clona.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + s);
                        System.out.println("Clona: " + clona);
                        break;
                    }
                }
                break;
            }
            case "DEEP": {
                String nume = tok[1].trim();
                for (Student s : studenti) {
                    if (s.getNume().equals(nume)) {
                        Student clona = s.deepClone();
                        clona.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + s);
                        System.out.println("Clona: " + clona);
                        break;
                    }
                }
                break;
            }
        }
    }
}
