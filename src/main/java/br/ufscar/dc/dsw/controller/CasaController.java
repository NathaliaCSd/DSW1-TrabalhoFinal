package br.ufscar.dc.dsw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.CasaRepository;
import br.ufscar.dc.dsw.dao.ReservaRepository;
import br.ufscar.dc.dsw.dao.UsuarioRepository;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/casas")
public class CasaController {

    private final CasaRepository casaRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository; 

    public CasaController(CasaRepository casaRepository,
                          ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository) {
        this.casaRepository = casaRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado();
        Pet pet = getPetLogado(request);

        if (usuario != null && usuario.isDonoDeHospedagem() && !usuario.isAdmin()) {
            List<Casa> minhasCasas = casaRepository.findByUsuario(usuario);
            List<Casa> outrasCasas = casaRepository.findByUsuarioNot(usuario);
            model.addAttribute("minhasCasas", minhasCasas);
            model.addAttribute("outrasCasas", outrasCasas);
            model.addAttribute("modoAnfitriao", true);
        } else {
            model.addAttribute("casas", casaRepository.findAll());
            model.addAttribute("modoAnfitriao", false);
        }

        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("petLogado", pet);
        return "casas";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(Model model) {
        // SecurityConfig já garante que só anfitrião ou admin chega aqui
        model.addAttribute("casa", new Casa());
        return "casa-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) return "redirect:/casas";

        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = casaRepository.findById(id).orElse(null);
        if (casa == null) return "redirect:/casas";

        // admin edita qualquer casa, anfitrião só edita as suas
        if (!usuario.isAdmin() && !casa.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/casas";
        }

        model.addAttribute("casa", casa);
        return "casa-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) return "redirect:/casas";

        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = casaRepository.findById(id).orElse(null);
        if (casa == null) return "redirect:/casas";

        // admin exclui qualquer casa, anfitrião só exclui as suas
        if (!usuario.isAdmin() && !casa.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/casas";
        }

        reservaRepository.deleteByCasaId(id);
        casaRepository.deleteById(id);
        return "redirect:/casas";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null || (!usuario.isAdmin() && !usuario.isDonoDeHospedagem())) {
            return "redirect:/casas";
        }

        String idParam       = request.getParameter("id");
        String nome          = request.getParameter("nome");
        String endereco      = request.getParameter("endereco");
        String descricao     = request.getParameter("descricao");
        String diariaParam   = request.getParameter("diaria");
        String capacidadeParam = request.getParameter("capacidade");
        String cidade        = request.getParameter("cidade");
        String estado        = request.getParameter("estado");
        String pais          = request.getParameter("pais");
        String dataDispParam = request.getParameter("dataDisponibilidade");
        String durMinParam   = request.getParameter("duracaoMinima");
        String durMaxParam   = request.getParameter("duracaoMaxima");

        // validação obrigatória
        if (nome == null || nome.isBlank() ||
            diariaParam == null || diariaParam.isBlank() ||
            capacidadeParam == null || capacidadeParam.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios.");
            model.addAttribute("casa", new Casa());
            return "casa-form";
        }

        Float diaria;
        Integer capacidade;
        try {
            diaria = Float.parseFloat(diariaParam);
            capacidade = Integer.parseInt(capacidadeParam);
        } catch (NumberFormatException e) {
            model.addAttribute("erro", "Diária e capacidade devem ser números válidos.");
            model.addAttribute("casa", new Casa());
            return "casa-form";
        }

        Casa casa;
        if (idParam != null && !idParam.isBlank()) {
            // edição
            Long id = Long.parseLong(idParam);
            casa = casaRepository.findById(id).orElse(null);
            if (casa == null) return "redirect:/casas";
            // anfitrião não pode editar casa de outro
            if (!usuario.isAdmin() && !casa.getUsuario().getId().equals(usuario.getId())) {
                return "redirect:/casas";
            }
        } else {
            // criação — associa ao usuário logado
            casa = new Casa();
            casa.setUsuario(usuario);
        }

        casa.setNome(nome);
        casa.setEndereco(endereco);
        casa.setDescricao(descricao);
        casa.setDiaria(diaria);
        casa.setCapacidade(capacidade);
        casa.setCidade(cidade);
        casa.setEstado(estado);
        casa.setPais(pais);

        //com parse seguro
        if (dataDispParam != null && !dataDispParam.isBlank()) {
            try { casa.setDataDisponibilidade(LocalDate.parse(dataDispParam)); }
            catch (Exception ignored) {}
        }
        if (durMinParam != null && !durMinParam.isBlank()) {
            try { casa.setDuracaoMinima(Integer.parseInt(durMinParam)); }
            catch (NumberFormatException ignored) {}
        }
        if (durMaxParam != null && !durMaxParam.isBlank()) {
            try { casa.setDuracaoMaxima(Integer.parseInt(durMaxParam)); }
            catch (NumberFormatException ignored) {}
        }

        casaRepository.save(casa);
        return "redirect:/casas";
    }

    // busca via Spring Security
    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth.getName().equals("anonymousUser")) return null;
        return usuarioRepository.findByLogin(auth.getName());
    }

    private Pet getPetLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Pet) session.getAttribute("petLogado");
    }
}