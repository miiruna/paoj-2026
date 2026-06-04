package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.exception.CititorNegasitException;
import com.pao.proiect.biblioteca.model.Cititor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CititorService {

    private static CititorService instance;

    private final Map<Integer, Cititor> cititori;

    private CititorService() {
        this.cititori = new HashMap<>();
    }

    public static CititorService getInstance() {
        if (instance == null) {
            instance = new CititorService();
        }
        return instance;
    }

    public void adaugaCititor(Cititor cititor) {
        AuditService.getInstance().logAction("adauga_cititor");
        if (cititor == null) {
            throw new IllegalArgumentException("Cititorul nu poate fi null.");
        }
        cititori.put(cititor.getId(), cititor);
    }

    public void stergeCititor(int id) throws CititorNegasitException {
        AuditService.getInstance().logAction("sterge_cititor");
        Cititor cititor = cititori.get(id);
        if (cititor == null) {
            throw new CititorNegasitException(id);
        }
        boolean areImprumututriActive = cititor.getIstoricImprumuturi().stream()
                .anyMatch(imp -> imp.esteActiv());
        if (areImprumututriActive) {
            throw new IllegalStateException("Cititorul " + cititor.getNumeComplet()
                    + " are împrumuturi active și nu poate fi eliminat.");
        }
        cititori.remove(id);
    }

    public Cititor cautaDupaId(int id) throws CititorNegasitException {
        AuditService.getInstance().logAction("cauta_cititor_dupa_id");
        Cititor cititor = cititori.get(id);
        if (cititor == null) {
            throw new CititorNegasitException(id);
        }
        return cititor;
    }

    public List<Cititor> listeazaToti() {
        AuditService.getInstance().logAction("listeaza_toti_cititorii");
        return new ArrayList<>(cititori.values());
    }
}
