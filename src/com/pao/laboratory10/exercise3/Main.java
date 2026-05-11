package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        List<Tranzactie> tranzactii = Arrays.asList(
            new Tranzactie(1,  1500.00, "2024-01-05", "CREDIT", "RO01INGB"),
            new Tranzactie(2,   200.00, "2024-01-10", "DEBIT",  "RO02BRDE"),
            new Tranzactie(3,  3200.00, "2024-01-20", "CREDIT", "RO01INGB"),
            new Tranzactie(4,   450.50, "2024-02-03", "DEBIT",  "RO03RZBR"),
            new Tranzactie(5,  2100.00, "2024-02-14", "CREDIT", "RO02BRDE"),
            new Tranzactie(6,   750.00, "2024-02-28", "DEBIT",  "RO01INGB"),
            new Tranzactie(7,  5000.00, "2024-03-01", "CREDIT", "RO04BTRL"),
            new Tranzactie(8,   320.75, "2024-03-15", "DEBIT",  "RO03RZBR"),
            new Tranzactie(9,  1800.00, "2024-03-22", "CREDIT", "RO02BRDE"),
            new Tranzactie(10,  980.00, "2024-03-30", "DEBIT",  "RO04BTRL"),
            new Tranzactie(11, 2500.00, "2024-04-08", "CREDIT", "RO01INGB"),
            new Tranzactie(12,  600.00, "2024-04-19", "DEBIT",  "RO02BRDE")
        );

        // 1. filter(tip == CREDIT)
        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip().equals("CREDIT"))
                .forEach(System.out::println);

        // 2. mapToDouble(suma).sum()
        System.out.println("\n=== 2. Total procesat ===");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", total);

        // 3. Collectors.groupingBy(luna, summingDouble(suma))
        System.out.println("\n=== 3. Total per luna ===");
        Map<String, Double> perLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)));
        perLuna.forEach((luna, s) ->
                System.out.printf(Locale.US, "%s: %.2f RON%n", luna, s));

        // 4. sorted(comparingDouble.reversed()).limit(3)
        System.out.println("\n=== 4. Top 3 tranzactii ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        // 5. map(contSursa).distinct().collect(toList())
        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturi = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturi);

        // 6. mapToDouble(suma).average()
        System.out.println("\n=== 6. Suma medie ===");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", medie);

        // 7. Collectors.groupingBy(luna) cu format extras
        System.out.println("\n=== 7. Extras de cont lunar ===");
        Map<String, List<Tranzactie>> grupate = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()));
        grupate.forEach((luna, lista) -> {
            double totalLuna = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), totalLuna);
        });
    }
}
