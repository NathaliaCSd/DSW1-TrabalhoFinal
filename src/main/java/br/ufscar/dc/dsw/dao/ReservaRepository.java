package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPetId(Long petId);
}
