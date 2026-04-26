package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public abstract class Persoana {

    protected String nume;
    protected String prenume;
    protected String email;

    public Persoana(String nume, String prenume, String email) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeComplet() {
        return prenume + " " + nume;
    }

    public abstract String getRol();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoana)) return false;
        Persoana p = (Persoana) o;
        return Objects.equals(email, p.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "nume='" + getNumeComplet() + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
