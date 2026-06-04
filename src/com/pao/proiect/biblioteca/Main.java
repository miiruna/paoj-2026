package com.pao.proiect.biblioteca;

import com.pao.proiect.biblioteca.exception.CarteNedisponibilaException;
import com.pao.proiect.biblioteca.exception.CititorNegasitException;
import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.model.ISBN;
import com.pao.proiect.biblioteca.model.Imprumut;
import com.pao.proiect.biblioteca.model.Rezervare;
import com.pao.proiect.biblioteca.model.Sectiune;
import com.pao.proiect.biblioteca.repository.AutorRepository;
import com.pao.proiect.biblioteca.repository.CarteRepository;
import com.pao.proiect.biblioteca.repository.CititorRepository;
import com.pao.proiect.biblioteca.repository.ImprumutRepository;
import com.pao.proiect.biblioteca.repository.SectiuneRepository;
import com.pao.proiect.biblioteca.service.BibliotecaJdbcService;
import com.pao.proiect.biblioteca.service.CarteService;
import com.pao.proiect.biblioteca.service.CititorService;
import com.pao.proiect.biblioteca.service.ImprumutService;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        CarteService carteService = CarteService.getInstance();
        CititorService cititorService = CititorService.getInstance();
        ImprumutService imprumutService = ImprumutService.getInstance();

        Sectiune fantasy = new Sectiune("Fantasy", "Speculative fiction featuring magic");
        Sectiune scienceFiction = new Sectiune("Science Fiction", "Speculative fiction based on futuristic concepts");
        Sectiune dystopian = new Sectiune("Dystopian", "Speculative fiction depicting a dystopian society");

        Autor maas = new Autor("Maas", "Sarah J.", "sarah.maas@fantasy.com", "Americană");
        Autor sanderson = new Autor("Sanderson", "Brandon", "brandon.sanderson@fantasy.com", "Americană");
        Autor brown = new Autor("Brown", "Pierce", "pierce.brown@dystopian.com", "Americană");

        ISBN isbn1 = new ISBN("9781639730957");
        ISBN isbn2 = new ISBN("9781635575569");
        ISBN isbn3 = new ISBN("9781250868282");
        ISBN isbn4 = new ISBN("9780345539809");

        Carte tog = new Carte(isbn1, "Throne of Glass", maas, fantasy, 3);
        Carte acotar = new Carte(isbn2, "A Court of Thorns and Roses", maas, fantasy, 2);
        Carte mistborn = new Carte(isbn3, "Mistborn: The Final Empire", sanderson, fantasy, 1);
        Carte redrising = new Carte(isbn4, "Red Rising", brown, scienceFiction, 2);

        System.out.println("=== ACȚIUNEA 1: Adaugă cărți noi în bibliotecă ===");
        carteService.adaugaCarte(tog);
        carteService.adaugaCarte(acotar);
        carteService.adaugaCarte(mistborn);
        carteService.adaugaCarte(redrising);
        System.out.println("Cărți adăugate: Throne of Glass, A Court of Thorns and Roses, Mistborn: The Final Empire, Red Rising");
        System.out.println("Lista tuturor cărților (sortate după titlu):");
        carteService.listeazaToate().forEach(System.out::println);

        System.out.println("\n=== ACȚIUNEA 2: Înregistrează cititori noi ===");
        Cititor ana = new Cititor("Talpeanu", "Ana", "ana.maria@email.com");
        Cititor miruna = new Cititor("Maxim", "Miruna", "miruna.maxim@email.com");
        Cititor manu = new Cititor("Tulbure", "Miruna", "miruna.tulbure@email.com");
        cititorService.adaugaCititor(ana);
        cititorService.adaugaCititor(miruna);
        cititorService.adaugaCititor(manu);
        System.out.println("Cititori înregistrați:");
        cititorService.listeazaToti().forEach(System.out::println);

        System.out.println("\n=== ACȚIUNEA 3: Împrumută o carte unui cititor ===");
        Imprumut imp1 = null;
        Imprumut imp2 = null;
        try {
            imp1 = imprumutService.imprumutaCarte(ana, mistborn);
            System.out.println("Împrumut creat: " + imp1);

            imp2 = imprumutService.imprumutaCarte(miruna, tog);
            System.out.println("Împrumut creat: " + imp2);

            imprumutService.imprumutaCarte(manu, mistborn);
        } catch (CarteNedisponibilaException e) {
            System.out.println("[EXCEPȚIE TRATATĂ] " + e.getMessage());

            Rezervare rezervare = new Rezervare(manu, mistborn);
            System.out.println("Rezervare creată pentru Manu: " + rezervare);
        }

        try {
            Imprumut imp3 = imprumutService.imprumutaCarte(manu, acotar);
            System.out.println("Împrumut creat: " + imp3);
        } catch (CarteNedisponibilaException e) {
            System.out.println("[EXCEPȚIE] " + e.getMessage());
        }

        System.out.println("\n=== ACȚIUNEA 4: Returnează o carte ===");
        try {
            System.out.println("Înainte de returnare — exemplare disponibile Mistborn: "
                    + mistborn.getExemplareDisponibile() + "/" + mistborn.getNumarTotalExemplare());
            imprumutService.returneazaCarte(imp1.getId());
            System.out.println("Cartea a fost returnată de " + ana.getNumeComplet());
            System.out.println("După returnare — exemplare disponibile Mistborn: "
                    + mistborn.getExemplareDisponibile() + "/" + mistborn.getNumarTotalExemplare());
        } catch (CititorNegasitException e) {
            System.out.println("[EXCEPȚIE] " + e.getMessage());
        }

        System.out.println("\n=== ACȚIUNEA 5: Caută cărți după autor ===");
        String numeAutor = maas.getNumeComplet();
        List<Carte> cartiMaas = carteService.cautaDupaAutor(numeAutor);
        System.out.println("Cărți scrise de " + numeAutor + ":");
        cartiMaas.forEach(c -> System.out.println("  - " + c.getTitlu()));

        System.out.println("\n=== ACȚIUNEA 6: Listează cărțile din secțiunea Fantasy ===");
        List<Carte> cartiFantasy = carteService.listeazaDupaSectiune(fantasy);
        System.out.println("Cărți în secțiunea \"" + fantasy.getNume() + "\":");
        cartiFantasy.forEach(c -> System.out.println("  - " + c.getTitlu() + " (disponibile: "
                + c.getExemplareDisponibile() + ")"));

        System.out.println("\n=== ACȚIUNEA 7: Istoricul împrumuturilor pentru Ana Talpeanu ===");
        List<Imprumut> istoricAna = imprumutService.getIstoricCititor(ana);
        if (istoricAna.isEmpty()) {
            System.out.println("Ana Talpeanu nu are niciun împrumut.");
        } else {
            istoricAna.forEach(System.out::println);
        }

        System.out.println("\n=== ACȚIUNEA 8: Verifică disponibilitatea cărților ===");
        System.out.println("Throne of Glass disponibil? " + carteService.esteDisponibila(isbn1));
        System.out.println("A Court of Thorns and Roses disponibil? " + carteService.esteDisponibila(isbn2));
        System.out.println("Mistborn disponibil? " + carteService.esteDisponibila(isbn3));
        System.out.println("Red Rising disponibil? " + carteService.esteDisponibila(isbn4));

        System.out.println("\n=== ACȚIUNEA 9: Top 3 cărți după număr de împrumuturi ===");
        try {
            imprumutService.imprumutaCarte(ana, redrising);
            imprumutService.imprumutaCarte(miruna, redrising);
        } catch (CarteNedisponibilaException e) {
            System.out.println("[EXCEPȚIE] " + e.getMessage());
        }
        List<Carte> topCarti = carteService.getCeleMailMultImprumutateCarte(3);
        System.out.println("Top 3 cele mai împrumutate cărți:");
        for (int i = 0; i < topCarti.size(); i++) {
            Carte c = topCarti.get(i);
            System.out.printf("  %d. %s — %d împrumuturi%n", i + 1, c.getTitlu(), c.getNumarTotalImprumuturi());
        }

        System.out.println("\n=== ACȚIUNEA 10: Elimină un cititor din sistem ===");

        try {
            cititorService.stergeCititor(manu.getId());
        } catch (CititorNegasitException e) {
            System.out.println("[EXCEPȚIE] " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("[BLOCAT] " + e.getMessage());
        }

        try {
            List<Imprumut> activeManu = imprumutService.getImprumututriActive().stream()
                    .filter(i -> i.getCititor().equals(manu))
                    .toList();
            for (Imprumut i : activeManu) {
                imprumutService.returneazaCarte(i.getId());
                System.out.println("Returnat: " + i.getCarte().getTitlu() + " de " + manu.getNumeComplet());
            }
            cititorService.stergeCititor(manu.getId());
            System.out.println("Cititorul " + manu.getNumeComplet() + " a fost eliminat din sistem.");
        } catch (CititorNegasitException e) {
            System.out.println("[EXCEPȚIE] " + e.getMessage());
        }

        System.out.println("\nCititori rămași în sistem:");
        cititorService.listeazaToti().forEach(System.out::println);

        System.out.println("\n=== DEMO COMPLET ===");
        ruleazaDemoEtapa2Jdbc(fantasy, maas, tog, ana);
    }

    private static void ruleazaDemoEtapa2Jdbc(Sectiune sectiune, Autor autor, Carte carte, Cititor cititor) {
        System.out.println("\n=== ETAPA II: JDBC, TRANZACȚII ȘI JOIN ===");

        SectiuneRepository sectiuneRepository = new SectiuneRepository();
        AutorRepository autorRepository = new AutorRepository();
        CititorRepository cititorRepository = new CititorRepository();
        CarteRepository carteRepository = new CarteRepository();
        ImprumutRepository imprumutRepository = new ImprumutRepository();

        try {
            if (sectiuneRepository.findById(sectiune.getNume()).isEmpty()) {
                sectiuneRepository.save(sectiune);
            }
            if (autorRepository.findById(autor.getEmail()).isEmpty()) {
                autorRepository.save(autor);
            }
            if (cititorRepository.findById(cititor.getId()).isEmpty()) {
                cititorRepository.save(cititor);
            }
            if (carteRepository.findById(carte.getIsbn().getValue()).isEmpty()) {
                carteRepository.save(carte);
            }

            carte.setTitlu(carte.getTitlu());
            carteRepository.update(carte);

            Imprumut imprumutJdbc = new Imprumut(cititor, carte);
            BibliotecaJdbcService.getInstance().imprumutaCarteCuTranzactie(imprumutJdbc);

            System.out.println("Cărți cu autori și secțiuni:");
            carteRepository.findCartiCuAutoriSiSectiuni().forEach(System.out::println);

            System.out.println("Împrumuturi active cu detalii:");
            imprumutRepository.findImprumuturiActiveCuDetalii().forEach(System.out::println);

            System.out.println("Număr împrumuturi active per cititor:");
            imprumutRepository.countImprumuturiActivePerCititor().forEach(System.out::println);

            System.out.println("Cele mai împrumutate cărți cu autor:");
            imprumutRepository.findCeleMaiImprumutateCartiCuAutor().forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Demo-ul JDBC nu a rulat deoarece baza de date nu este disponibilă/configurată încă.");
            System.out.println("Verifică resources/db.properties și rulează resources/schema.sql în MySQL.");
            System.out.println("Detaliu JDBC: " + e.getMessage());
        }
    }
}
