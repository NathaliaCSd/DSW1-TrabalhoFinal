package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.CasaDAO;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/casas")
public class CasaController {

    private final CasaDAO casaDAO = new CasaDAO();

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login.jsp";
        }
        List<Casa> lista = casaDAO.getAll();
        model.addAttribute("casas", lista);
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
        Casa casa = casaDAO.get(id);
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
        casaDAO.delete(new Casa(id));
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

        if (idParam == null || idParam.isBlank()) {
            Casa casa = new Casa(nome, endereco, descricao, diaria, capacidade);
            casaDAO.insert(casa);
        } else {
            Long id = Long.parseLong(idParam);
            Casa casa = new Casa(id, nome, endereco, descricao, diaria, capacidade);
            casaDAO.update(casa);
        }
        return "redirect:/casas";
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
