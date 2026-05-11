package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii banda;
    private static int contor = 0;

    public ATMThread(int atmId, CoadaTranzactii banda) {
        this.atmId = atmId;
        this.banda = banda;
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            int id;
            synchronized (ATMThread.class) { id = ++contor; }
            double suma = 100.0 * (id + 1);
            Tranzactie t = new Tranzactie(id, suma, "2024-01-15");
            try {
                banda.adauga(t, atmId);
                System.out.printf(Locale.US, "[ATM-%d] trimite: Tranzactie #%d %.2f RON%n", atmId, id, suma);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
