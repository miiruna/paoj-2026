package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class CustomCollectors {

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            Map<String, Long> byCountry = new HashMap<>();
            Map<String, Long> byChannel = new HashMap<>();
            BigDecimal total = BigDecimal.ZERO;
            List<Transaction> all = new ArrayList<>();

            void add(Transaction tx) {
                byCountry.merge(tx.getCountry(), 1L, Long::sum);
                byChannel.merge(tx.getChannel(), 1L, Long::sum);
                total = total.add(tx.getAmount());
                all.add(tx);
            }

            Agg combine(Agg other) {
                other.byCountry.forEach((k, v) -> byCountry.merge(k, v, Long::sum));
                other.byChannel.forEach((k, v) -> byChannel.merge(k, v, Long::sum));
                total = total.add(other.total);
                all.addAll(other.all);
                return this;
            }

            Snapshot finish() {
                List<Transaction> top = new ArrayList<>(all);
                top.sort(Comparator.comparing(Transaction::getAmount).reversed()
                        .thenComparingInt(Transaction::getId));
                return new Snapshot(byCountry, byChannel, total,
                        top.subList(0, Math.min(topN, top.size())));
            }
        }

        return Collector.of(
                Agg::new,
                Agg::add,
                Agg::combine,
                Agg::finish,
                Collector.Characteristics.UNORDERED
        );
    }
}
