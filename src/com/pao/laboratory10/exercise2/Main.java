package com.pao.laboratory10.exercise2;

//import com.pao.laboratory10.exercise1.Tranzactie;
//import com.pao.laboratory10.exercise1.TipTranzactie;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tok = sc.nextLine().trim().split("\\s+");
            lista.add(new Tranzactie(Integer.parseInt(tok[0]),
                    Double.parseDouble(tok[1]), tok[2],
                    TipTranzactie.valueOf(tok[3])));
        }

        Comparator<Tranzactie> bySuma = Comparator.comparingDouble(Tranzactie::getSuma);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] tok = line.split("\\s+");
            String cmd = tok[0];

            switch (cmd) {
                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();
                    for (Tranzactie t : lista) ids.add(t.getId());
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                }
                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> map = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String month = t.getData().substring(0, 7);
                        map.computeIfAbsent(month, k -> new double[2]);
                        if (t.getTip() == TipTranzactie.CREDIT) map.get(month)[0] += t.getSuma();
                        else                                     map.get(month)[1] += t.getSuma();
                    }
                    for (Map.Entry<String, double[]> e : map.entrySet()) {
                        System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                e.getKey(), e.getValue()[0], e.getValue()[1]);
                    }
                    break;
                }
                case "TOP": {
                    int k = Integer.parseInt(tok[1]);
                    List<Tranzactie> copy = new ArrayList<>(lista);
                    copy.sort(bySuma.reversed());
                    System.out.println("Top " + k + ":");
                    copy.subList(0, Math.min(k, copy.size())).forEach(System.out::println);
                    break;
                }
                case "SORT_ASC": {
                    Collections.sort(lista, bySuma);
                    lista.forEach(System.out::println);
                    break;
                }
                case "SORT_DESC": {
                    Collections.sort(lista, bySuma.reversed());
                    lista.forEach(System.out::println);
                    break;
                }
                case "REVERSE": {
                    Collections.reverse(lista);
                    lista.forEach(System.out::println);
                    break;
                }
                case "MIN_MAX": {
                    System.out.println("MIN: " + Collections.min(lista, bySuma));
                    System.out.println("MAX: " + Collections.max(lista, bySuma));
                    break;
                }
                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : lista) lista.remove(t);
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
            }
        }
    }
}
