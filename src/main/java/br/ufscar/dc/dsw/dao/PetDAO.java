package br.ufscar.dc.dsw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;

public class PetDAO extends GenericDAO {

    public void insert(Pet pet) {
        String sql = "INSERT INTO Pet (nome, raca, idade, porte, castrado, descricao, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = this.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pet.getNome());
            statement.setString(2, pet.getRaca());
            statement.setInt(3, pet.getIdade());
            statement.setString(4, pet.getPorte());
            statement.setBoolean(5, pet.getCastrado());
            statement.setString(6, pet.getDescricao());
            statement.setLong(7, pet.getUsuario().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Pet pet) {
        String sql = "UPDATE Pet SET nome = ?, raca = ?, idade = ?, porte = ?, castrado = ?, descricao = ?, usuario_id = ? WHERE id = ?";
        try (Connection conn = this.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pet.getNome());
            statement.setString(2, pet.getRaca());
            statement.setInt(3, pet.getIdade());
            statement.setString(4, pet.getPorte());
            statement.setBoolean(5, pet.getCastrado());
            statement.setString(6, pet.getDescricao());
            statement.setLong(7, pet.getUsuario().getId());
            statement.setLong(8, pet.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Pet pet) {
        String sql = "DELETE FROM Pet WHERE id = ?";
        try (Connection conn = this.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, pet.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pet> getAll() {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.nome AS usuario_nome, u.login AS usuario_login, u.senha AS usuario_senha, u.papel AS usuario_papel FROM Pet p JOIN Usuario u ON p.usuario_id = u.id";
        try (Connection conn = this.getConnection();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                lista.add(createPet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<Pet> getAllByUsuario(Long usuarioId) {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.nome AS usuario_nome, u.login AS usuario_login, u.senha AS usuario_senha, u.papel AS usuario_papel FROM Pet p JOIN Usuario u ON p.usuario_id = u.id WHERE p.usuario_id = ?";
        try (Connection conn = this.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(createPet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Pet get(Long id) {
        Pet pet = null;
        String sql = "SELECT p.*, u.nome AS usuario_nome, u.login AS usuario_login, u.senha AS usuario_senha, u.papel AS usuario_papel FROM Pet p JOIN Usuario u ON p.usuario_id = u.id WHERE p.id = ?";
        try (Connection conn = this.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
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
        Long usuarioId = resultSet.getLong("usuario_id");
        String usuarioNome = resultSet.getString("usuario_nome");
        String usuarioLogin = resultSet.getString("usuario_login");
        String usuarioSenha = resultSet.getString("usuario_senha");
        String usuarioPapel = resultSet.getString("usuario_papel");
        Usuario usuario = new Usuario(usuarioId, usuarioNome, usuarioLogin, usuarioSenha, usuarioPapel);
        return new Pet(id, nome, raca, idade, porte, castrado, descricao, usuario);
    }
}
