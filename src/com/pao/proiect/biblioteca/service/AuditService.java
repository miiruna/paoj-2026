package com.pao.proiect.biblioteca.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class AuditService {

    private static AuditService instance;
    private static final Path AUDIT_FILE = Path.of("audit.csv");

    private AuditService() {
        initializeFile();
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logAction(String actionName) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                AUDIT_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(actionName + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut scrie în audit.csv.", e);
        }
    }

    private void initializeFile() {
        if (Files.exists(AUDIT_FILE)) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                AUDIT_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write("nume_actiune,timestamp");
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut initializa audit.csv.", e);
        }
    }
}
