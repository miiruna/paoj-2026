package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON
        LinkedList<Tranzactie> coada = new LinkedList<>();
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] tok = line.split("\\s+");
            String cmd = tok[0];

            switch (cmd) {
                case "ENQUEUE": {
                    coada.addLast(new Tranzactie(Integer.parseInt(tok[1]),
                            Double.parseDouble(tok[2]), tok[3],
                            TipTranzactie.valueOf(tok[4])));
                    break;
                }
                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Procesat: " + coada.removeFirst());
                    }
                    break;
                }
                case "PUSH": {
                    coada.addFirst(new Tranzactie(Integer.parseInt(tok[1]),
                            Double.parseDouble(tok[2]), tok[3],
                            TipTranzactie.valueOf(tok[4])));
                    break;
                }
                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Extras: " + coada.removeFirst());
                    }
                    break;
                }
                case "REMOVE_DEBIT": {
                    int count = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().tip == TipTranzactie.DEBIT) {
                            itr.remove();
                            count++;
                        }
                    }
                    System.out.println("Eliminat " + count + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double threshold = Double.parseDouble(tok[1]);
                    int count = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().suma < threshold) {
                            itr.remove();
                            count++;
                        }
                    }
                    System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", count, threshold);
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }
            }
        }
    }
}
