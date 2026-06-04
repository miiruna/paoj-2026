package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Sectiune;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectiuneRepository implements Repository<Sectiune, String> {

    @Override
    public void save(Sectiune sectiune) throws SQLException {
        String sql = "INSERT INTO sectiuni(nume, descriere) VALUES (?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, sectiune.getNume());
            statement.setString(2, sectiune.getDescriere());
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Sectiune> findById(String nume) throws SQLException {
        String sql = "SELECT nume, descriere FROM sectiuni WHERE nume = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, nume);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapSectiune(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Sectiune> findAll() throws SQLException {
        String sql = "SELECT nume, descriere FROM sectiuni";
        List<Sectiune> sectiuni = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                sectiuni.add(mapSectiune(resultSet));
            }
        }
        return sectiuni;
    }

    @Override
    public void update(Sectiune sectiune) throws SQLException {
        String sql = "UPDATE sectiuni SET descriere = ? WHERE nume = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, sectiune.getDescriere());
            statement.setString(2, sectiune.getNume());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(String nume) throws SQLException {
        String sql = "DELETE FROM sectiuni WHERE nume = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, nume);
            statement.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Sectiune mapSectiune(ResultSet resultSet) throws SQLException {
        return new Sectiune(resultSet.getString("nume"), resultSet.getString("descriere"));
    }
}
