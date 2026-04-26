package com.pao.proiect.biblioteca.model;

import java.time.LocalDate;
import java.util.Objects;

public class Imprumut {

    private static int contor = 1;

    private final int id;
    private final Cititor cititor;
    private final Carte carte;
    private final LocalDate dataImprumut;
    private final LocalDate dataScadenta;
    private LocalDate dataReturnare;

    public Imprumut(Cititor cititor, Carte carte) {
        this.id = contor++;
        this.cititor = cititor;
        this.carte = carte;
        this.dataImprumut = LocalDate.now();
        this.dataScadenta = LocalDate.now().plusDays(14);
        this.dataReturnare = null;
    }

    public int getId() {
        return id;
    }

    public Cititor getCititor() {
        return cititor;
    }

    public Carte getCarte() {
        return carte;
    }

    public LocalDate getDataImprumut() {
        return dataImprumut;
    }

    public LocalDate getDataScadenta() {
        return dataScadenta;
    }

    public LocalDate getDataReturnare() {
        return dataReturnare;
    }

    public boolean esteActiv() {
        return dataReturnare == null;
    }

    public void returneaza() {
        this.dataReturnare = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Imprumut)) return false;
        Imprumut i = (Imprumut) o;
        return id == i.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Imprumut{" +
                "id=" + id +
                ", cititor=" + cititor.getNumeComplet() +
                ", carte='" + carte.getTitlu() + '\'' +
                ", dataImprumut=" + dataImprumut +
                ", dataScadenta=" + dataScadenta +
                ", dataReturnare=" + (dataReturnare != null ? dataReturnare : "ACTIV") +
                '}';
    }
}
