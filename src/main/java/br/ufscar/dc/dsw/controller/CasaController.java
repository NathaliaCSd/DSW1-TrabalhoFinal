package br.ufscar.dc.dsw.controller;

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
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller // Controlador Spring MVC que atende URLs com prefixo /casas.
@RequestMapping("/casas")
public class CasaController {

    private final CasaRepository casaRepository; // Repositório JPA injetado pelo Spring.
    private final ReservaRepository reservaRepository;

    public CasaController(CasaRepository casaRepository, ReservaRepository reservaRepository) {
        this.casaRepository = casaRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        Pet pet = getPetLogado(request);

        if (usuario != null && usuario.isDonoDeHospedagem() && !usuario.isAdmin()) {
            List<Casa> minhasCasas = casaRepository.findByUsuario(usuario);
            List<Casa> outrasCasas = casaRepository.findByUsuarioNot(usuario);
            model.addAttribute("minhasCasas", minhasCasas);
            model.addAttribute("outrasCasas", outrasCasas);
            model.addAttribute("modoAnfitriao", true);
        } else {
            // admin e visitantes veem tudo junto
            model.addAttribute("casas", casaRepository.findAll());
            model.addAttribute("modoAnfitriao", false);
        }

        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("petLogado", pet);
        return "casas";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || (!usuario.isDonoDeHospedagem() && !usuario.isAdmin())) {
            return "redirect:/casas";
        }
        model.addAttribute("casa", new Casa());
        return "casa-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null)
            return "redirect:/casas";

        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = casaRepository.findById(id).orElse(null);
        if (casa == null)
            return "redirect:/casas";

        //admin edita qualquer casa, anfitrião só edita as suas
        if (!usuario.isAdmin() && !casa.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/casas";
        }

        model.addAttribute("casa", casa);
        return "casa-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null)
            return "redirect:/casas";

        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = casaRepository.findById(id).orElse(null);
        if (casa == null)
            return "redirect:/casas";

        //garantindo os privilegios 
        //admin exclui qualquer casa, anfitrião só exclui as suas
        if (!usuario.isAdmin() && !casa.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/casas";
        }

        reservaRepository.deleteByCasaId(id);
        casaRepository.deleteById(id);
        return "redirect:/casas";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        //admin e anfitrião podem salvar
        if (usuario == null || (!usuario.isAdmin() && !usuario.isDonoDeHospedagem())) {
            return "redirect:/casas";
        }

        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String endereco = request.getParameter("endereco");
        String descricao = request.getParameter("descricao");
        String diariaParam = request.getParameter("diaria");
        String capacidadeParam = request.getParameter("capacidade");

        Float diaria;
        Integer capacidade;
        try {
            diaria = Float.parseFloat(diariaParam);
            capacidade = Integer.parseInt(capacidadeParam);
        } catch (NumberFormatException e) {
            model.addAttribute("erro", "Preencha corretamente diária e capacidade.");
            model.addAttribute("casa", new Casa());
            return "casa-form";
        }

        Casa casa;
        if (idParam != null && !idParam.isBlank()) {
            Long id = Long.parseLong(idParam);
            casa = casaRepository.findById(id).orElse(new Casa());
            // anfitrião não pode editar casa de outro anfitriao
            if (!usuario.isAdmin() && !casa.getUsuario().getId().equals(usuario.getId())) {
                return "redirect:/casas";
            }
        } else {
            casa = new Casa();
            casa.setUsuario(usuario); // ✅ associa ao anfitrião logado na criação
        }

        casa.setNome(nome);
        casa.setEndereco(endereco);
        casa.setDescricao(descricao);
        casa.setDiaria(diaria);
        casa.setCapacidade(capacidade);
        casaRepository.save(casa);
        return "redirect:/casas";
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
