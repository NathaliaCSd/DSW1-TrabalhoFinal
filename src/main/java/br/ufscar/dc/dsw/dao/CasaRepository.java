package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Casa;

// Repositório Spring Data JPA para a entidade Casa.
// Herdando JpaRepository, já oferece métodos CRUD como findAll(), findById(), save() e deleteById().
public interface CasaRepository extends JpaRepository<Casa, Long> {
}
