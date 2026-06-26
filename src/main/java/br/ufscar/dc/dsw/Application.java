package br.ufscar.dc.dsw;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.UsuarioRepository;
import br.ufscar.dc.dsw.domain.Usuario;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner dataInitializer(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin"));
                admin.setPapeis(Arrays.asList("ADMIN"));
                usuarioRepository.save(admin);
            }
        };
    }
}