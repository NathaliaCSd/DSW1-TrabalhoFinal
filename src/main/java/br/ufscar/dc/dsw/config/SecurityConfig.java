package br.ufscar.dc.dsw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // público — sem login
                .requestMatchers("/", "/casas", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                // só ADMIN
                .requestMatchers("/usuario/lista", "/usuario/novo", "/usuario/edicao", "/usuario/excluir").hasRole("ADMIN")
                // só DONO_PET
                .requestMatchers("/pets/**", "/reservas/**").hasAnyRole("DONO_PET", "ADMIN")
                // só DONO_HOSPEDAGEM
                .requestMatchers("/casas/novo", "/casas/edicao", "/casas/excluir", "/casas/salvar").hasAnyRole("DONO_HOSPEDAGEM", "ADMIN")
                // qualquer logado
                .requestMatchers("/usuario/perfil").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/usuario/login")
                .loginProcessingUrl("/usuario/login")
                .usernameParameter("login")
                .passwordParameter("senha")
                .defaultSuccessUrl("/casas", true)
                .failureUrl("/usuario/login?erro=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/usuario/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }
}