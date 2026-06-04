package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.exception.CarteNedisponibilaException;
import com.pao.proiect.biblioteca.exception.CititorNegasitException;
import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.model.Imprumut;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImprumutService {

    private static ImprumutService instance;

    private final List<Imprumut> imprumuturi;

    private ImprumutService() {
        this.imprumuturi = new ArrayList<>();
    }

    public static ImprumutService getInstance() {
        if (instance == null) {
            instance = new ImprumutService();
        }
        return instance;
    }

    public Imprumut imprumutaCarte(Cititor cititor, Carte carte)
            throws CarteNedisponibilaException {
        AuditService.getInstance().logAction("imprumuta_carte");
        if (cititor == null) {
            throw new IllegalArgumentException("Cititorul nu poate fi null.");
        }
        if (carte == null) {
            throw new IllegalArgumentException("Cartea nu poate fi null.");
        }
        if (!carte.esteDisponibila()) {
            throw new CarteNedisponibilaException(carte.getTitlu());
        }
        carte.decrementeazaDisponibil();
        Imprumut imprumut = new Imprumut(cititor, carte);
        imprumuturi.add(imprumut);
        cititor.adaugaImprumut(imprumut);
        return imprumut;
    }

    public void returneazaCarte(int idImprumut) throws CititorNegasitException {
        AuditService.getInstance().logAction("returneaza_carte");
        Imprumut imprumut = imprumuturi.stream()
                .filter(i -> i.getId() == idImprumut)
                .findFirst()
                .orElseThrow(() -> new CititorNegasitException(
                        "Împrumutul cu id-ul " + idImprumut + " nu a fost găsit."));
        if (!imprumut.esteActiv()) {
            throw new IllegalStateException("Împrumutul cu id-ul " + idImprumut + " a fost deja returnat.");
        }
        imprumut.returneaza();
        imprumut.getCarte().incrementeazaDisponibil();
    }

    public List<Imprumut> getIstoricCititor(Cititor cititor) {
        AuditService.getInstance().logAction("afiseaza_istoric_imprumuturi_cititor");
        return cititor.getIstoricImprumuturi();
    }

    public List<Imprumut> getImprumututriActive() {
        AuditService.getInstance().logAction("listeaza_imprumuturi_active");
        return imprumuturi.stream()
                .filter(Imprumut::esteActiv)
                .collect(Collectors.toList());
    }

    public List<Imprumut> listeazaToate() {
        AuditService.getInstance().logAction("listeaza_toate_imprumuturile");
        return new ArrayList<>(imprumuturi);
    }
}
