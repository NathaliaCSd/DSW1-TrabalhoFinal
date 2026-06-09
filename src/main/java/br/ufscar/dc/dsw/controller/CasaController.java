package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.CasaRepository;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/casas")
public class CasaController {

    private final CasaRepository casaRepository;

    public CasaController(CasaRepository casaRepository) {
        this.casaRepository = casaRepository;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        Pet pet = getPetLogado(request);
        List<Casa> lista = casaRepository.findAll();
        model.addAttribute("casas", lista);
        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("petLogado", pet);
        return "casas";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/casas";
        }
        return "casa-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/casas";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = casaRepository.findById(id).orElse(null);
        model.addAttribute("casa", casa);
        return "casa-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/casas";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        casaRepository.deleteById(id);
        return "redirect:/casas";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
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
            return "casa-form";
        }

        Casa casa = new Casa();
        if (idParam != null && !idParam.isBlank()) {
            Long id = Long.parseLong(idParam);
            casa = casaRepository.findById(id).orElse(new Casa());
            casa.setId(id);
        }
        casa.setNome(nome);
        casa.setEndereco(endereco);
        casa.setDescricao(descricao);
        casa.setDiaria(diaria);
        casa.setCapacidade(capacidade);
        if (casa.getUsuario() == null) {
            casa.setUsuario(usuario);
        }
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
