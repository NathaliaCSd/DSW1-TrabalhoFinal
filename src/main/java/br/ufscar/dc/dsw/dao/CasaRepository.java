package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Casa;

public interface CasaRepository extends JpaRepository<Casa, Long> {
}
