package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Usuario;

// Repositório Spring Data JPA para a entidade Casa.
// Herdando JpaRepository, já oferece métodos CRUD como findAll(), findById(), save() e deleteById().
public interface CasaRepository extends JpaRepository<Casa, Long> {
    // CasaRepository.java
List<Casa> findByUsuario(Usuario usuario);
List<Casa> findByUsuarioNot(Usuario usuario);

}

