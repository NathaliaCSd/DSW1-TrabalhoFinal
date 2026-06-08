package br.ufscar.dc.dsw;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import br.ufscar.dc.dsw.dao.CasaRepository;
import br.ufscar.dc.dsw.dao.PetRepository;
import br.ufscar.dc.dsw.dao.ReservaRepository;
import br.ufscar.dc.dsw.dao.UsuarioRepository;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Reserva;
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
    public CommandLineRunner dataInitializer(UsuarioRepository usuarioRepository, CasaRepository casaRepository,
            PetRepository petRepository, ReservaRepository reservaRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario("Administrador", "admin", "admin", "ADMIN");
                Usuario user = new Usuario("Cliente", "user", "user", "USER");
                usuarioRepository.save(admin);
                usuarioRepository.save(user);
            }

            if (casaRepository.count() == 0) {
                Casa casa1 = new Casa("Casa do Luar", "Rua das Flores, 123", "Ambiente acolhedor para cães de pequeno e médio porte.", 120.00f, 3);
                Casa casa2 = new Casa("Refúgio Pet Feliz", "Av. dos Pinheiros, 45", "Casa ampla com quintal seguro para passeios diários.", 180.00f, 5);
                casaRepository.save(casa1);
                casaRepository.save(casa2);
            }

            if (petRepository.count() == 0) {
                Pet pet1 = new Pet("Luna", "Shih Tzu", 4, "Pequeno", true, "Adora carinho e tem energia tranquila.");
                Pet pet2 = new Pet("Bongo", "Labrador", 3, "Grande", false, "Muito amigável e gosta de brincar com crianças.");
                petRepository.save(pet1);
                petRepository.save(pet2);
            }

            if (reservaRepository.count() == 0 && petRepository.count() > 0 && casaRepository.count() > 0) {
                Pet pet = petRepository.findAll().get(0);
                Casa casa = casaRepository.findAll().get(0);
                LocalDate inicio = LocalDate.now().plusDays(1);
                LocalDate fim = inicio.plusDays(2);
                Reserva reserva = new Reserva(pet, casa, inicio, fim, 2 * casa.getDiaria());
                reservaRepository.save(reserva);
            }
        };
    }
}
