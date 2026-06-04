package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CititorRepository implements Repository<Cititor, Integer> {

    @Override
    public void save(Cititor cititor) throws SQLException {
        String sql = "INSERT INTO cititori(id, nume, prenume, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            fillStatement(statement, cititor);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Cititor> findById(Integer id) throws SQLException {
        String sql = "SELECT id, nume, prenume, email FROM cititori WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCititor(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Cititor> findAll() throws SQLException {
        String sql = "SELECT id, nume, prenume, email FROM cititori";
        List<Cititor> cititori = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                cititori.add(mapCititor(resultSet));
            }
        }
        return cititori;
    }

    @Override
    public void update(Cititor cititor) throws SQLException {
        String sql = "UPDATE cititori SET nume = ?, prenume = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, cititor.getNume());
            statement.setString(2, cititor.getPrenume());
            statement.setString(3, cititor.getEmail());
            statement.setInt(4, cititor.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM cititori WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private void fillStatement(PreparedStatement statement, Cititor cititor) throws SQLException {
        statement.setInt(1, cititor.getId());
        statement.setString(2, cititor.getNume());
        statement.setString(3, cititor.getPrenume());
        statement.setString(4, cititor.getEmail());
    }

    private Cititor mapCititor(ResultSet resultSet) throws SQLException {
        return new Cititor(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                resultSet.getString("prenume"),
                resultSet.getString("email"));
    }
}
