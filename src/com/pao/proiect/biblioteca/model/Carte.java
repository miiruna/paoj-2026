package com.pao.proiect.biblioteca.model;

import java.util.Objects;

public class Carte implements Comparable<Carte> {

    private ISBN isbn;
    private String titlu;
    private Autor autor;
    private Sectiune sectiune;
    private int numarTotalExemplare;
    private int exemplareDisponibile;
    private int numarTotalImprumuturi;

    public Carte(ISBN isbn, String titlu, Autor autor, Sectiune sectiune, int numarTotalExemplare) {
        this.isbn = isbn;
        this.titlu = titlu;
        this.autor = autor;
        this.sectiune = sectiune;
        this.numarTotalExemplare = numarTotalExemplare;
        this.exemplareDisponibile = numarTotalExemplare;
        this.numarTotalImprumuturi = 0;
    }

    public Carte(ISBN isbn, String titlu, Autor autor, Sectiune sectiune,
                 int numarTotalExemplare, int exemplareDisponibile, int numarTotalImprumuturi) {
        this.isbn = isbn;
        this.titlu = titlu;
        this.autor = autor;
        this.sectiune = sectiune;
        this.numarTotalExemplare = numarTotalExemplare;
        this.exemplareDisponibile = exemplareDisponibile;
        this.numarTotalImprumuturi = numarTotalImprumuturi;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Sectiune getSectiune() {
        return sectiune;
    }

    public void setSectiune(Sectiune sectiune) {
        this.sectiune = sectiune;
    }

    public int getNumarTotalExemplare() {
        return numarTotalExemplare;
    }

    public int getExemplareDisponibile() {
        return exemplareDisponibile;
    }

    public int getNumarTotalImprumuturi() {
        return numarTotalImprumuturi;
    }

    public boolean esteDisponibila() {
        return exemplareDisponibile > 0;
    }

    public void decrementeazaDisponibil() {
        if (exemplareDisponibile > 0) {
            exemplareDisponibile--;
            numarTotalImprumuturi++;
        }
    }

    public void incrementeazaDisponibil() {
        if (exemplareDisponibile < numarTotalExemplare) {
            exemplareDisponibile++;
        }
    }

    @Override
    public int compareTo(Carte alta) {
        return this.titlu.compareToIgnoreCase(alta.titlu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carte)) return false;
        Carte c = (Carte) o;
        return Objects.equals(isbn, c.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Carte{" +
                "isbn=" + isbn +
                ", titlu='" + titlu + '\'' +
                ", autor=" + autor.getNumeComplet() +
                ", sectiune='" + sectiune.getNume() + '\'' +
                ", disponibile=" + exemplareDisponibile + "/" + numarTotalExemplare +
                ", imprumuturi=" + numarTotalImprumuturi +
                '}';
    }
}
