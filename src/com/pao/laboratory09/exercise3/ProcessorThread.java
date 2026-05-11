package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii banda;
    private int total = 0;

    public ProcessorThread(CoadaTranzactii banda) {
        this.banda = banda;
    }

    public int getTotal() { return total; }

    @Override
    public void run() {
        while (activ || !banda.isEmpty()) {
            try {
                Tranzactie t = banda.extrage();
                total++;
                System.out.printf(Locale.US, "[Processor] Factura #%d - %.2f RON | %s%n",
                        t.getId(), t.getSuma(), t.getData());
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
