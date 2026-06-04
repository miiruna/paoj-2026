package com.pao.proiect.biblioteca.service;

import com.pao.proiect.biblioteca.model.Imprumut;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BibliotecaJdbcService {

    private static BibliotecaJdbcService instance;

    private BibliotecaJdbcService() {
    }

    public static synchronized BibliotecaJdbcService getInstance() {
        if (instance == null) {
            instance = new BibliotecaJdbcService();
        }
        return instance;
    }

    public void imprumutaCarteCuTranzactie(Imprumut imprumut) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        boolean initialAutoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);
            int disponibile = getExemplareDisponibile(connection, imprumut.getCarte().getIsbn().getValue());
            if (disponibile <= 0) {
                throw new SQLException("Cartea nu are exemplare disponibile in baza de date.");
            }

            salveazaImprumut(connection, imprumut);
            actualizeazaDisponibilitateCarte(connection, imprumut.getCarte().getIsbn().getValue());
            connection.commit();
            AuditService.getInstance().logAction("jdbc_imprumuta_carte_tranzactie");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(initialAutoCommit);
        }
    }

    private int getExemplareDisponibile(Connection connection, String isbn) throws SQLException {
        String sql = "SELECT exemplare_disponibile FROM carti WHERE isbn = ? FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("exemplare_disponibile");
                }
                throw new SQLException("Cartea cu ISBN-ul " + isbn + " nu exista in baza de date.");
            }
        }
    }

    private void salveazaImprumut(Connection connection, Imprumut imprumut) throws SQLException {
        String sql = """
                INSERT INTO imprumuturi(id, cititor_id, carte_isbn, data_imprumut, data_scadenta, data_returnare)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            statement.executeUpdate();
        }
    }

    private void actualizeazaDisponibilitateCarte(Connection connection, String isbn) throws SQLException {
        String sql = """
                UPDATE carti
                SET exemplare_disponibile = exemplare_disponibile - 1,
                    numar_total_imprumuturi = numar_total_imprumuturi + 1
                WHERE isbn = ? AND exemplare_disponibile > 0
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Nu s-a putut actualiza disponibilitatea cartii.");
            }
        }
    }
}
