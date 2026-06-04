package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.model.ISBN;
import com.pao.proiect.biblioteca.model.Imprumut;
import com.pao.proiect.biblioteca.model.Sectiune;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImprumutRepository implements Repository<Imprumut, Integer> {

    @Override
    public void save(Imprumut imprumut) throws SQLException {
        String sql = """
                INSERT INTO imprumuturi(id, cititor_id, carte_isbn, data_imprumut, data_scadenta, data_returnare)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            fillStatement(statement, imprumut);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Imprumut> findById(Integer id) throws SQLException {
        String sql = selectWithJoins() + " WHERE i.id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapImprumut(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Imprumut> findAll() throws SQLException {
        List<Imprumut> imprumuturi = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(selectWithJoins());
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                imprumuturi.add(mapImprumut(resultSet));
            }
        }
        return imprumuturi;
    }

    @Override
    public void update(Imprumut imprumut) throws SQLException {
        String sql = """
                UPDATE imprumuturi
                SET cititor_id = ?, carte_isbn = ?, data_imprumut = ?, data_scadenta = ?, data_returnare = ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, imprumut.getCititor().getId());
            statement.setString(2, imprumut.getCarte().getIsbn().getValue());
            statement.setDate(3, Date.valueOf(imprumut.getDataImprumut()));
            statement.setDate(4, Date.valueOf(imprumut.getDataScadenta()));
            if (imprumut.getDataReturnare() == null) {
                statement.setDate(5, null);
            } else {
                statement.setDate(5, Date.valueOf(imprumut.getDataReturnare()));
            }
            statement.setInt(6, imprumut.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM imprumuturi WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<String> findImprumuturiActiveCuDetalii() throws SQLException {
        String sql = selectWithJoins() + " WHERE i.data_returnare IS NULL ORDER BY i.data_scadenta";
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rezultate.add(resultSet.getString("cititor_prenume") + " "
                        + resultSet.getString("cititor_nume") + " - "
                        + resultSet.getString("titlu") + ", scadenta: "
                        + resultSet.getDate("data_scadenta"));
            }
        }
        return rezultate;
    }

    public List<String> countImprumuturiActivePerCititor() throws SQLException {
        String sql = """
                SELECT c.id, c.prenume, c.nume, COUNT(i.id) AS imprumuturi_active
                FROM cititori c
                LEFT JOIN imprumuturi i ON c.id = i.cititor_id AND i.data_returnare IS NULL
                GROUP BY c.id, c.prenume, c.nume
                ORDER BY imprumuturi_active DESC, c.nume
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rezultate.add(resultSet.getString("prenume") + " "
                        + resultSet.getString("nume") + " are "
                        + resultSet.getInt("imprumuturi_active") + " imprumuturi active");
            }
        }
        return rezultate;
    }

    public List<String> findCeleMaiImprumutateCartiCuAutor() throws SQLException {
        String sql = """
                SELECT ca.titlu, a.prenume, a.nume, COUNT(i.id) AS total_imprumuturi
                FROM carti ca
                JOIN autori a ON ca.autor_email = a.email
                LEFT JOIN imprumuturi i ON ca.isbn = i.carte_isbn
                GROUP BY ca.isbn, ca.titlu, a.prenume, a.nume
                ORDER BY total_imprumuturi DESC, ca.titlu
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rezultate.add(resultSet.getString("titlu") + " - "
                        + resultSet.getString("prenume") + " " + resultSet.getString("nume")
                        + ": " + resultSet.getInt("total_imprumuturi") + " imprumuturi");
            }
        }
        return rezultate;
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private void fillStatement(PreparedStatement statement, Imprumut imprumut) throws SQLException {
        statement.setInt(1, imprumut.getId());
        statement.setInt(2, imprumut.getCititor().getId());
        statement.setString(3, imprumut.getCarte().getIsbn().getValue());
        statement.setDate(4, Date.valueOf(imprumut.getDataImprumut()));
        statement.setDate(5, Date.valueOf(imprumut.getDataScadenta()));
        if (imprumut.getDataReturnare() == null) {
            statement.setDate(6, null);
        } else {
            statement.setDate(6, Date.valueOf(imprumut.getDataReturnare()));
        }
    }

    private String selectWithJoins() {
        return """
                SELECT i.id, i.data_imprumut, i.data_scadenta, i.data_returnare,
                       ci.id AS cititor_id, ci.nume AS cititor_nume, ci.prenume AS cititor_prenume,
                       ci.email AS cititor_email,
                       ca.isbn, ca.titlu, ca.numar_total_exemplare, ca.exemplare_disponibile,
                       ca.numar_total_imprumuturi,
                       a.email AS autor_email, a.nume AS autor_nume, a.prenume AS autor_prenume,
                       a.nationalitate,
                       s.nume AS sectiune_nume, s.descriere AS sectiune_descriere
                FROM imprumuturi i
                JOIN cititori ci ON i.cititor_id = ci.id
                JOIN carti ca ON i.carte_isbn = ca.isbn
                JOIN autori a ON ca.autor_email = a.email
                JOIN sectiuni s ON ca.sectiune_nume = s.nume
                """;
    }

    private Imprumut mapImprumut(ResultSet resultSet) throws SQLException {
        Cititor cititor = new Cititor(
                resultSet.getInt("cititor_id"),
                resultSet.getString("cititor_nume"),
                resultSet.getString("cititor_prenume"),
                resultSet.getString("cititor_email"));
        Autor autor = new Autor(
                resultSet.getString("autor_nume"),
                resultSet.getString("autor_prenume"),
                resultSet.getString("autor_email"),
                resultSet.getString("nationalitate"));
        Sectiune sectiune = new Sectiune(
                resultSet.getString("sectiune_nume"),
                resultSet.getString("sectiune_descriere"));
        Carte carte = new Carte(
                new ISBN(resultSet.getString("isbn")),
                resultSet.getString("titlu"),
                autor,
                sectiune,
                resultSet.getInt("numar_total_exemplare"),
                resultSet.getInt("exemplare_disponibile"),
                resultSet.getInt("numar_total_imprumuturi"));
        Date dataReturnare = resultSet.getDate("data_returnare");
        return new Imprumut(
                resultSet.getInt("id"),
                cititor,
                carte,
                resultSet.getDate("data_imprumut").toLocalDate(),
                resultSet.getDate("data_scadenta").toLocalDate(),
                dataReturnare == null ? null : dataReturnare.toLocalDate());
    }
}
