package br.ufscar.dc.dsw.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Reserva;
import br.ufscar.dc.dsw.domain.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPetId(Long petId);

    void deleteByCasaId(Long casaId);

    List<Reserva> findByCasaUsuarioAndDataInicioAfter(Usuario usuario, LocalDate data);

}
