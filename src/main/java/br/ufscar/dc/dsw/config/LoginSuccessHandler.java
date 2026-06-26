package br.ufscar.dc.dsw.config;

import br.ufscar.dc.dsw.dao.UsuarioRepository;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication) throws IOException {
        Usuario usuario = usuarioRepository.findByLogin(authentication.getName());

        if (usuario.isAdmin()) {
            response.sendRedirect("/usuario/lista");
        } else if (usuario.isDonoDePet()) {
            response.sendRedirect("/pets");
        } else {
            response.sendRedirect("/casas");
        }
    }
}