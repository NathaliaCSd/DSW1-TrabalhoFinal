package br.ufscar.dc.dsw.config;

import br.ufscar.dc.dsw.dao.UsuarioRepository;
import br.ufscar.dc.dsw.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }
        return new org.springframework.security.core.userdetails.User(
            usuario.getLogin(),
            usuario.getSenha(),
            usuario.getPapeis().stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_" + p))
                .collect(Collectors.toList())
        );
    }
}