package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.ISBN;
import com.pao.proiect.biblioteca.model.Sectiune;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarteRepository implements Repository<Carte, String> {

    @Override
    public void save(Carte carte) throws SQLException {
        String sql = """
                INSERT INTO carti(isbn, titlu, autor_email, sectiune_nume,
                                  numar_total_exemplare, exemplare_disponibile, numar_total_imprumuturi)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            fillStatement(statement, carte);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Carte> findById(String isbn) throws SQLException {
        String sql = """
                SELECT c.isbn, c.titlu, c.numar_total_exemplare, c.exemplare_disponibile,
                       c.numar_total_imprumuturi,
                       a.email AS autor_email, a.nume AS autor_nume, a.prenume AS autor_prenume,
                       a.nationalitate,
                       s.nume AS sectiune_nume, s.descriere AS sectiune_descriere
                FROM carti c
                JOIN autori a ON c.autor_email = a.email
                JOIN sectiuni s ON c.sectiune_nume = s.nume
                WHERE c.isbn = ?
                """;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, isbn);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCarte(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Carte> findAll() throws SQLException {
        String sql = """
                SELECT c.isbn, c.titlu, c.numar_total_exemplare, c.exemplare_disponibile,
                       c.numar_total_imprumuturi,
                       a.email AS autor_email, a.nume AS autor_nume, a.prenume AS autor_prenume,
                       a.nationalitate,
                       s.nume AS sectiune_nume, s.descriere AS sectiune_descriere
                FROM carti c
                JOIN autori a ON c.autor_email = a.email
                JOIN sectiuni s ON c.sectiune_nume = s.nume
                ORDER BY c.titlu
                """;
        List<Carte> carti = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                carti.add(mapCarte(resultSet));
            }
        }
        return carti;
    }

    @Override
    public void update(Carte carte) throws SQLException {
        String sql = """
                UPDATE carti
                SET titlu = ?, autor_email = ?, sectiune_nume = ?, numar_total_exemplare = ?,
                    exemplare_disponibile = ?, numar_total_imprumuturi = ?
                WHERE isbn = ?
                """;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, carte.getTitlu());
            statement.setString(2, carte.getAutor().getEmail());
            statement.setString(3, carte.getSectiune().getNume());
            statement.setInt(4, carte.getNumarTotalExemplare());
            statement.setInt(5, carte.getExemplareDisponibile());
            statement.setInt(6, carte.getNumarTotalImprumuturi());
            statement.setString(7, carte.getIsbn().getValue());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM carti WHERE isbn = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        }
    }

    public List<String> findCartiCuAutoriSiSectiuni() throws SQLException {
        String sql = """
                SELECT c.titlu, a.prenume, a.nume, s.nume AS sectiune
                FROM carti c
                JOIN autori a ON c.autor_email = a.email
                JOIN sectiuni s ON c.sectiune_nume = s.nume
                ORDER BY c.titlu
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rezultate.add(resultSet.getString("titlu") + " - "
                        + resultSet.getString("prenume") + " " + resultSet.getString("nume")
                        + " (" + resultSet.getString("sectiune") + ")");
            }
        }
        return rezultate;
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private void fillStatement(PreparedStatement statement, Carte carte) throws SQLException {
        statement.setString(1, carte.getIsbn().getValue());
        statement.setString(2, carte.getTitlu());
        statement.setString(3, carte.getAutor().getEmail());
        statement.setString(4, carte.getSectiune().getNume());
        statement.setInt(5, carte.getNumarTotalExemplare());
        statement.setInt(6, carte.getExemplareDisponibile());
        statement.setInt(7, carte.getNumarTotalImprumuturi());
    }

    private Carte mapCarte(ResultSet resultSet) throws SQLException {
        Autor autor = new Autor(
                resultSet.getString("autor_nume"),
                resultSet.getString("autor_prenume"),
                resultSet.getString("autor_email"),
                resultSet.getString("nationalitate"));
        Sectiune sectiune = new Sectiune(
                resultSet.getString("sectiune_nume"),
                resultSet.getString("sectiune_descriere"));
        return new Carte(
                new ISBN(resultSet.getString("isbn")),
                resultSet.getString("titlu"),
                autor,
                sectiune,
                resultSet.getInt("numar_total_exemplare"),
                resultSet.getInt("exemplare_disponibile"),
                resultSet.getInt("numar_total_imprumuturi"));
    }
}
