package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // TODO: Manual demo for bonus requirements.
        List<Transaction> data = List.of(
                new Transaction(1,  new BigDecimal("1200.00"), LocalDate.of(2026, 1, 10), "RO", "WEB"),
                new Transaction(2,  new BigDecimal("340.00"),  LocalDate.of(2026, 1, 15), "RU", "ATM"),
                new Transaction(3,  new BigDecimal("8500.00"), LocalDate.of(2026, 2,  1), "NG", "APP"),
                new Transaction(4,  new BigDecimal("75.00"),   LocalDate.of(2026, 2,  5), "DE", "WEB"),
                new Transaction(5,  new BigDecimal("1200.00"), LocalDate.of(2026, 3,  1), "RO", "CRYPTO"),
                new Transaction(6,  new BigDecimal("500.00"),  LocalDate.of(2026, 3, 10), "RU", "POS"),
                new Transaction(7,  new BigDecimal("2300.00"), LocalDate.of(2026, 4,  1), "DE", "APP"),
                new Transaction(8,  new BigDecimal("90.00"),   LocalDate.of(2026, 4, 20), "RO", "ATM")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println("=== Interogare 1: Top 3 tranzactii dupa suma ===");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println("\n=== Interogare 2: Numar tranzactii pe tara (descendent) ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("  %s: %d%n", e.getKey(), e.getValue()));

        System.out.println("\n=== Interogare 3: Canale ordonate dupa numar de tranzactii ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("  %s: %d%n", e.getKey(), e.getValue()));

        System.out.println("\n=== Interogare 4: Total suma tranzactii ===");
        System.out.println("  Total: " + snap.getTotalAmount() + " RON");
    }
}
