package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByLogin(String login);
    Usuario findByLoginAndSenha(String login, String senha);
}
