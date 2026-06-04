package com.pao.proiect.biblioteca.model;

import java.time.LocalDate;
import java.util.Objects;

public class Rezervare {

    private static int contor = 1;

    private final int id;
    private final Cititor cititor;
    private final Carte carte;
    private final LocalDate dataRezervare;
    private boolean activa;

    public Rezervare(Cititor cititor, Carte carte) {
        this.id = contor++;
        this.cititor = cititor;
        this.carte = carte;
        this.dataRezervare = LocalDate.now();
        this.activa = true;
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

    public LocalDate getDataRezervare() {
        return dataRezervare;
    }

    public boolean isActiva() {
        return activa;
    }

    public void anuleaza() {
        this.activa = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rezervare)) return false;
        Rezervare r = (Rezervare) o;
        return id == r.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "id=" + id +
                ", cititor=" + cititor.getNumeComplet() +
                ", carte='" + carte.getTitlu() + '\'' +
                ", dataRezervare=" + dataRezervare +
                ", activa=" + activa +
                '}';
    }
}
