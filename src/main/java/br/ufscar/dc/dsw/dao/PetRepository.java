package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
