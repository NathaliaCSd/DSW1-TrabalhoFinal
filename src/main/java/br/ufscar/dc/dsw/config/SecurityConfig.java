package br.ufscar.dc.dsw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/casas", "/usuario/login", "/css/**", "/js/**", "/images/**",
                                "/fonts/**")
                        .permitAll()
                        .requestMatchers("/usuario/lista", "/usuario/novo", "/usuario/edicao", "/usuario/excluir",
                                "/usuario/salvar")
                        .hasRole("ADMIN")
                        .requestMatchers("/pets/**", "/reservas/**").hasAnyRole("DONO_PET", "ADMIN")
                        .requestMatchers("/casas/novo", "/casas/edicao", "/casas/excluir", "/casas/salvar")
                        .hasAnyRole("DONO_HOSPEDAGEM", "ADMIN")
                        .requestMatchers("/reservas/api/**").permitAll()
                        .requestMatchers("/usuario/perfil").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/usuario/login")
                        .loginProcessingUrl("/usuario/login")
                        .usernameParameter("login")
                        .passwordParameter("senha")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/usuario/login?erro=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/usuario/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .permitAll());
        return http.build();
    }
}