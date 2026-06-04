package com.pao.proiect.biblioteca.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cititor extends Persoana {

    private static int contor = 1;

    private final int id;
    private List<Imprumut> istoricImprumuturi;

    public Cititor(String nume, String prenume, String email) {
        super(nume, prenume, email);
        this.id = contor++;
        this.istoricImprumuturi = new ArrayList<>();
    }

    public Cititor(int id, String nume, String prenume, String email) {
        super(nume, prenume, email);
        this.id = id;
        this.istoricImprumuturi = new ArrayList<>();
        if (id >= contor) {
            contor = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public List<Imprumut> getIstoricImprumuturi() {
        return Collections.unmodifiableList(istoricImprumuturi);
    }

    public void adaugaImprumut(Imprumut imprumut) {
        istoricImprumuturi.add(imprumut);
    }

    @Override
    public String getRol() {
        return "Cititor";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cititor)) return false;
        Cititor c = (Cititor) o;
        return id == c.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cititor{" +
                "id=" + id +
                ", numeComplet='" + getNumeComplet() + '\'' +
                ", email='" + email + '\'' +
                ", nrImprumuturi=" + istoricImprumuturi.size() +
                '}';
    }
}
