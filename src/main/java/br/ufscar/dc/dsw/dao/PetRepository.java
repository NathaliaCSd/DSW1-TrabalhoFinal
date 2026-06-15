package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByDono(Usuario dono);
}
