package com.pao.laboratory09.exercise2;

//import com.pao.laboratory09.exercise1.TipTranzactie;

import com.pao.laboratory09.exercise1.TipTranzactie;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());

        int[] ids = new int[n];
        double[] sume = new double[n];
        String[] date = new String[n];
        TipTranzactie[] tipuri = new TipTranzactie[n];

        for (int i = 0; i < n; i++) {
            String[] tok = sc.nextLine().trim().split("\\s+");
            ids[i] = Integer.parseInt(tok[0]);
            sume[i] = Double.parseDouble(tok[1]);
            date[i] = tok[2];
            tipuri[i] = TipTranzactie.valueOf(tok[3]);
        }

        new File("output").mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ids[i]).array());
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(sume[i]).array());
                byte[] dataBytes = new byte[10];
                byte[] src = date[i].getBytes();
                System.arraycopy(src, 0, dataBytes, 0, src.length);
                for (int j = src.length; j < 10; j++) dataBytes[j] = ' ';
                dos.write(dataBytes);
                dos.write(tipuri[i] == TipTranzactie.CREDIT ? 0 : 1);
                dos.write(0);
                dos.write(new byte[8]);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] tok = line.split("\\s+");
                switch (tok[0]) {
                    case "READ": {
                        int idx = Integer.parseInt(tok[1]);
                        System.out.println(readRecord(raf, idx));
                        break;
                    }
                    case "UPDATE": {
                        int idx = Integer.parseInt(tok[1]);
                        String statusStr = tok[2];
                        byte statusByte = statusToByte(statusStr);
                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.write(statusByte);
                        System.out.println("Updated [" + idx + "]: " + statusStr);
                        break;
                    }
                    case "PRINT_ALL": {
                        long totalRecords = raf.length() / RECORD_SIZE;
                        for (int i = 0; i < totalRecords; i++) {
                            System.out.println(readRecord(raf, i));
                        }
                        break;
                    }
                }
            }
        }
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] buf = new byte[RECORD_SIZE];
        raf.readFully(buf);
        ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        int id = bb.getInt();
        double suma = bb.getDouble();
        byte[] dataBytes = new byte[10];
        bb.get(dataBytes);
        String data = new String(dataBytes).trim();
        int tipByte = bb.get() & 0xFF;
        int statusByte = bb.get() & 0xFF;
        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
        String[] statusNames = {"PENDING", "PROCESSED", "REJECTED"};
        String status = statusNames[statusByte];
        return String.format(java.util.Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }

    private static byte statusToByte(String status) {
        switch (status) {
            case "PROCESSED": return 1;
            case "REJECTED":  return 2;
            default:          return 0;
        }
    }
}
