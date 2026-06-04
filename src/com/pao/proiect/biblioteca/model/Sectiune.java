package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public class Sectiune {

    private String nume;
    private String descriere;

    public Sectiune(String nume, String descriere) {
        this.nume = nume;
        this.descriere = descriere;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sectiune)) return false;
        Sectiune s = (Sectiune) o;
        return Objects.equals(nume, s.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume);
    }

    @Override
    public String toString() {
        return "Sectiune{" +
                "nume='" + nume + '\'' +
                ", descriere='" + descriere + '\'' +
                '}';
    }
}
