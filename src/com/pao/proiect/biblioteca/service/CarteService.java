package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.ISBN;
import com.pao.proiect.biblioteca.model.Sectiune;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class CarteService {

    private static CarteService instance;

    private final Map<String, Carte> cartiDupaIsbn;

    private final Map<String, List<Carte>> cartiDupaAutor;

    private final TreeSet<Carte> cartiSortate;

    private CarteService() {
        this.cartiDupaIsbn = new HashMap<>();
        this.cartiDupaAutor = new HashMap<>();
        this.cartiSortate = new TreeSet<>();
    }

    public static CarteService getInstance() {
        if (instance == null) {
            instance = new CarteService();
        }
        return instance;
    }

    public void adaugaCarte(Carte carte) {
        AuditService.getInstance().logAction("adauga_carte");
        if (carte == null) {
            throw new IllegalArgumentException("Cartea nu poate fi null.");
        }
        if (cartiDupaIsbn.containsKey(carte.getIsbn().getValue())) {
            throw new IllegalArgumentException("O carte cu ISBN-ul " + carte.getIsbn() + " există deja.");
        }
        cartiDupaIsbn.put(carte.getIsbn().getValue(), carte);
        cartiSortate.add(carte);

        String cheieAutor = carte.getAutor().getNumeComplet();
        cartiDupaAutor.computeIfAbsent(cheieAutor, k -> new ArrayList<>()).add(carte);
    }

    public void stergeCarte(ISBN isbn) {
        AuditService.getInstance().logAction("sterge_carte");
        Carte carte = cartiDupaIsbn.remove(isbn.getValue());
        if (carte == null) {
            throw new IllegalArgumentException("Cartea cu ISBN-ul " + isbn + " nu există în sistem.");
        }
        cartiSortate.remove(carte);
        String cheieAutor = carte.getAutor().getNumeComplet();
        List<Carte> listaCarte = cartiDupaAutor.get(cheieAutor);
        if (listaCarte != null) {
            listaCarte.remove(carte);
            if (listaCarte.isEmpty()) {
                cartiDupaAutor.remove(cheieAutor);
            }
        }
    }

    public Carte cautaDupaIsbn(ISBN isbn) {
        AuditService.getInstance().logAction("cauta_carte_dupa_isbn");
        return cartiDupaIsbn.get(isbn.getValue());
    }

    public List<Carte> cautaDupaAutor(String numeAutor) {
        AuditService.getInstance().logAction("cauta_carti_dupa_autor");
        return cartiDupaAutor.getOrDefault(numeAutor, Collections.emptyList());
    }

    public List<Carte> listeazaDupaSectiune(Sectiune sectiune) {
        AuditService.getInstance().logAction("listeaza_carti_dupa_sectiune");
        List<Carte> rezultat = new ArrayList<>();
        for (Carte c : cartiSortate) {
            if (c.getSectiune().equals(sectiune)) {
                rezultat.add(c);
            }
        }
        return rezultat;
    }

    public List<Carte> listeazaToate() {
        AuditService.getInstance().logAction("listeaza_toate_cartile");
        return new ArrayList<>(cartiSortate);
    }

    public boolean esteDisponibila(ISBN isbn) {
        AuditService.getInstance().logAction("verifica_disponibilitate_carte");
        Carte carte = cartiDupaIsbn.get(isbn.getValue());
        return carte != null && carte.esteDisponibila();
    }

    public List<Carte> getCeleMailMultImprumutateCarte(int top) {
        AuditService.getInstance().logAction("afiseaza_carti_cu_cele_mai_multe_imprumuturi");
        List<Carte> toate = new ArrayList<>(cartiDupaIsbn.values());
        toate.sort((a, b) -> {
            int cmp = b.getNumarTotalImprumuturi() - a.getNumarTotalImprumuturi();
            if (cmp != 0) return cmp;
            return a.getTitlu().compareToIgnoreCase(b.getTitlu());
        });
        return toate.subList(0, Math.min(top, toate.size()));
    }
}
