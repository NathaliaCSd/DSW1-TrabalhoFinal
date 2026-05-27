package br.ufscar.dc.dsw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.Casa;

public class CasaDAO extends GenericDAO {

    public void insert(Casa casa) {
        String sql = "INSERT INTO Casa (nome, endereco, descricao, diaria, capacidade) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, casa.getNome());
            statement.setString(2, casa.getEndereco());
            statement.setString(3, casa.getDescricao());
            statement.setFloat(4, casa.getDiaria());
            statement.setInt(5, casa.getCapacidade());
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Casa casa) {
        String sql = "UPDATE Casa SET nome = ?, endereco = ?, descricao = ?, diaria = ?, capacidade = ? WHERE id = ?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, casa.getNome());
            statement.setString(2, casa.getEndereco());
            statement.setString(3, casa.getDescricao());
            statement.setFloat(4, casa.getDiaria());
            statement.setInt(5, casa.getCapacidade());
            statement.setLong(6, casa.getId());
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Casa casa) {
        String sql = "DELETE FROM Casa WHERE id = ?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, casa.getId());
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Casa> getAll() {
        List<Casa> listaCasas = new ArrayList<>();
        String sql = "SELECT * FROM Casa";
        try {
            Connection conn = this.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                listaCasas.add(createCasa(resultSet));
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaCasas;
    }

    public Casa get(Long id) {
        Casa casa = null;
        String sql = "SELECT * FROM Casa WHERE id = ?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                casa = createCasa(resultSet);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return casa;
    }

    public List<Casa> search(String nome, Integer dias, Float maxTotal) {
        List<Casa> resultado = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Casa");
        if (nome != null && !nome.isBlank()) {
            sql.append(" WHERE LOWER(nome) LIKE ?");
        }
        if (maxTotal != null) {
            sql.append(nome != null && !nome.isBlank() ? " AND diaria * ? <= ?" : " WHERE diaria * ? <= ?");
        }
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (nome != null && !nome.isBlank()) {
                statement.setString(idx++, "%" + nome.toLowerCase() + "%");
            }
            if (maxTotal != null) {
                statement.setInt(idx++, dias != null ? dias : 1);
                statement.setFloat(idx++, maxTotal);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultado.add(createCasa(resultSet));
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    private Casa createCasa(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String nome = resultSet.getString("nome");
        String endereco = resultSet.getString("endereco");
        String descricao = resultSet.getString("descricao");
        Float diaria = resultSet.getFloat("diaria");
        Integer capacidade = resultSet.getInt("capacidade");
        return new Casa(id, nome, endereco, descricao, diaria, capacidade);
    }
}
