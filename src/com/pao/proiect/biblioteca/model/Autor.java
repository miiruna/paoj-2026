package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public class Autor extends Persoana {

    private String nationalitate;

    public Autor(String nume, String prenume, String email, String nationalitate) {
        super(nume, prenume, email);
        this.nationalitate = nationalitate;
    }

    public String getNationalitate() {
        return nationalitate;
    }

    public void setNationalitate(String nationalitate) {
        this.nationalitate = nationalitate;
    }

    @Override
    public String getRol() {
        return "Autor";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nationalitate);
    }

    @Override
    public String toString() {
        return "Autor{" +
                "numeComplet='" + getNumeComplet() + '\'' +
                ", email='" + email + '\'' +
                ", nationalitate='" + nationalitate + '\'' +
                '}';
    }
}
