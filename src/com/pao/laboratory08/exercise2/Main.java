package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        List<Student> studenti = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                studenti.add(new Student(parts[0].trim(),
                        Integer.parseInt(parts[1].trim()),
                        new Adresa(parts[2].trim(), parts[3].trim())));
            }
        }

        Scanner sc = new Scanner(System.in);
        int prag = Integer.parseInt(sc.nextLine().trim());

        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) filtrati.add(s);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rezultate.txt"))) {
            for (Student s : filtrati) {
                bw.write(s.toString());
                bw.newLine();
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student s : filtrati) System.out.println(s);
        System.out.println();
        System.out.println("Scris in: rezultate.txt");
    }
}

