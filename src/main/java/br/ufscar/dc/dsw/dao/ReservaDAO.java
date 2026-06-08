package br.ufscar.dc.dsw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Reserva;

public class ReservaDAO extends GenericDAO {

    public void insert(Reserva reserva) {
        String sql = "INSERT INTO Reserva (pet_id, casa_id, data_inicio, data_fim, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, reserva.getPet().getId());
            statement.setLong(2, reserva.getCasa().getId());
            statement.setDate(3, java.sql.Date.valueOf(reserva.getDataInicio()));
            statement.setDate(4, java.sql.Date.valueOf(reserva.getDataFim()));
            statement.setFloat(5, reserva.getTotal());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reserva> getAll() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, p.nome AS pet_nome, c.nome AS casa_nome, c.endereco AS casa_endereco, c.descricao AS casa_descricao, c.diaria AS casa_diaria, c.capacidade AS casa_capacidade FROM Reserva r JOIN Pet p ON r.pet_id = p.id JOIN Casa c ON r.casa_id = c.id";
        try (Connection conn = this.getConnection(); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                lista.add(createReserva(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<Reserva> getAllByPet(Long petId) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, p.nome AS pet_nome, c.nome AS casa_nome, c.endereco AS casa_endereco, c.descricao AS casa_descricao, c.diaria AS casa_diaria, c.capacidade AS casa_capacidade FROM Reserva r JOIN Pet p ON r.pet_id = p.id JOIN Casa c ON r.casa_id = c.id WHERE r.pet_id = ?";
        try (Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, petId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(createReserva(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    private Reserva createReserva(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long petId = resultSet.getLong("pet_id");
        String petNome = resultSet.getString("pet_nome");
        Long casaId = resultSet.getLong("casa_id");
        String casaNome = resultSet.getString("casa_nome");
        String casaEndereco = resultSet.getString("casa_endereco");
        String casaDescricao = resultSet.getString("casa_descricao");
        Float casaDiaria = resultSet.getFloat("casa_diaria");
        Integer casaCapacidade = resultSet.getInt("casa_capacidade");
        LocalDate dataInicio = resultSet.getDate("data_inicio").toLocalDate();
        LocalDate dataFim = resultSet.getDate("data_fim").toLocalDate();
        Float total = resultSet.getFloat("total");

        Pet pet = new Pet(petId);
        pet.setNome(petNome);
        Casa casa = new Casa(casaId, casaNome, casaEndereco, casaDescricao, casaDiaria, casaCapacidade);
        return new Reserva(id, pet, casa, dataInicio, dataFim, total);
    }
}
