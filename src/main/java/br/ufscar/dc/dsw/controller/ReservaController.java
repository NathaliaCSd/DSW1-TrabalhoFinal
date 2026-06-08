package br.ufscar.dc.dsw.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.CasaRepository;
import br.ufscar.dc.dsw.dao.ReservaRepository;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Reserva;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final CasaRepository casaRepository;

    public ReservaController(ReservaRepository reservaRepository, CasaRepository casaRepository) {
        this.reservaRepository = reservaRepository;
        this.casaRepository = casaRepository;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        Pet pet = getPetLogado(request);
        List<Reserva> lista = (usuario != null && "ADMIN".equals(usuario.getPapel()))
                ? reservaRepository.findAll()
                : pet != null ? reservaRepository.findByPetId(pet.getId()) : null;
        model.addAttribute("reservas", lista);
        model.addAttribute("petLogado", pet);
        model.addAttribute("usuarioLogado", usuario);
        return "reservas";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(HttpServletRequest request, Model model) {
        Pet pet = getPetLogado(request);
        if (pet == null) {
            return "redirect:/pets";
        }
        Long casaId = Long.parseLong(request.getParameter("casaId"));
        Casa casa = casaRepository.findById(casaId).orElse(null);
        if (casa == null) {
            return "redirect:/casas";
        }
        model.addAttribute("casa", casa);
        model.addAttribute("pet", pet);
        return "reserva-form";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        Pet pet = getPetLogado(request);
        if (pet == null) {
            return "redirect:/pets";
        }
        String casaIdParam = request.getParameter("casaId");
        String dataInicioParam = request.getParameter("dataInicio");
        String dataFimParam = request.getParameter("dataFim");

        if (casaIdParam == null || dataInicioParam == null || dataFimParam == null
                || dataInicioParam.isBlank() || dataFimParam.isBlank()) {
            model.addAttribute("erro", "Preencha todos os dados da reserva.");
            return "reserva-form";
        }

        Long casaId = Long.parseLong(casaIdParam);
        Casa casa = casaRepository.findById(casaId).orElse(null);
        if (casa == null) {
            return "redirect:/casas";
        }

        LocalDate dataInicio;
        LocalDate dataFim;
        try {
            dataInicio = LocalDate.parse(dataInicioParam);
            dataFim = LocalDate.parse(dataFimParam);
        } catch (Exception e) {
            model.addAttribute("erro", "Datas inválidas.");
            model.addAttribute("casa", casa);
            return "reserva-form";
        }

        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
        if (dias < 1) {
            model.addAttribute("erro", "A data de fim deve ser posterior à data de início.");
            model.addAttribute("casa", casa);
            return "reserva-form";
        }

        Float total = dias * casa.getDiaria();
        Reserva reserva = new Reserva(pet, casa, dataInicio, dataFim, total);
        reservaRepository.save(reserva);
        return "redirect:/reservas";
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }

    private Pet getPetLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Pet) session.getAttribute("petLogado");
    }
}
