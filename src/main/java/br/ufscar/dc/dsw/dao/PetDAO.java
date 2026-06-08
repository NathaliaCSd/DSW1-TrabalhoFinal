package br.ufscar.dc.dsw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.Pet;

public class PetDAO extends GenericDAO {

    public Pet insert(Pet pet) {
        String sql = "INSERT INTO Pet (nome, raca, idade, porte, castrado, descricao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, pet.getNome());
            statement.setString(2, pet.getRaca());
            statement.setInt(3, pet.getIdade());
            statement.setString(4, pet.getPorte());
            statement.setBoolean(5, pet.getCastrado());
            statement.setString(6, pet.getDescricao());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    pet.setId(keys.getLong(1));
                }
            }
            return pet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Pet pet) {
        String sql = "UPDATE Pet SET nome = ?, raca = ?, idade = ?, porte = ?, castrado = ?, descricao = ? WHERE id = ?";
        try (Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pet.getNome());
            statement.setString(2, pet.getRaca());
            statement.setInt(3, pet.getIdade());
            statement.setString(4, pet.getPorte());
            statement.setBoolean(5, pet.getCastrado());
            statement.setString(6, pet.getDescricao());
            statement.setLong(7, pet.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Pet pet) {
        String sql = "DELETE FROM Pet WHERE id = ?";
        try (Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, pet.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pet> getAll() {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pet";
        try (Connection conn = this.getConnection(); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                lista.add(createPet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Pet get(Long id) {
        Pet pet = null;
        String sql = "SELECT * FROM Pet WHERE id = ?";
        try (Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    pet = createPet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pet;
    }

    private Pet createPet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String nome = resultSet.getString("nome");
        String raca = resultSet.getString("raca");
        Integer idade = resultSet.getInt("idade");
        String porte = resultSet.getString("porte");
        Boolean castrado = resultSet.getBoolean("castrado");
        String descricao = resultSet.getString("descricao");
        return new Pet(id, nome, raca, idade, porte, castrado, descricao);
    }
}
