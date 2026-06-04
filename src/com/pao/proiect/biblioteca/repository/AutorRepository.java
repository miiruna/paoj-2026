package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutorRepository implements Repository<Autor, String> {

    @Override
    public void save(Autor autor) throws SQLException {
        String sql = "INSERT INTO autori(email, nume, prenume, nationalitate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            fillStatement(statement, autor);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Autor> findById(String email) throws SQLException {
        String sql = "SELECT email, nume, prenume, nationalitate FROM autori WHERE email = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapAutor(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Autor> findAll() throws SQLException {
        String sql = "SELECT email, nume, prenume, nationalitate FROM autori";
        List<Autor> autori = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                autori.add(mapAutor(resultSet));
            }
        }
        return autori;
    }

    @Override
    public void update(Autor autor) throws SQLException {
        String sql = "UPDATE autori SET nume = ?, prenume = ?, nationalitate = ? WHERE email = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, autor.getNume());
            statement.setString(2, autor.getPrenume());
            statement.setString(3, autor.getNationalitate());
            statement.setString(4, autor.getEmail());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(String email) throws SQLException {
        String sql = "DELETE FROM autori WHERE email = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, email);
            statement.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private void fillStatement(PreparedStatement statement, Autor autor) throws SQLException {
        statement.setString(1, autor.getEmail());
        statement.setString(2, autor.getNume());
        statement.setString(3, autor.getPrenume());
        statement.setString(4, autor.getNationalitate());
    }

    private Autor mapAutor(ResultSet resultSet) throws SQLException {
        return new Autor(
                resultSet.getString("nume"),
                resultSet.getString("prenume"),
                resultSet.getString("email"),
                resultSet.getString("nationalitate"));
    }
}
