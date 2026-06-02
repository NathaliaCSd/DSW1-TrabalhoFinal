package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.PetDAO;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetDAO petDAO = new PetDAO();

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login.jsp";
        }
        List<Pet> lista = "ADMIN".equals(usuario.getPapel()) ? petDAO.getAll()
                : petDAO.getAllByUsuario(usuario.getId());
        model.addAttribute("pets", lista);
        return "pets";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login.jsp";
        }
        return "pet-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login.jsp";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petDAO.get(id);
        if (pet == null || (!"ADMIN".equals(usuario.getPapel()) && !usuario.getId().equals(pet.getUsuario().getId()))) {
            return "redirect:/pets";
        }
        model.addAttribute("pet", pet);
        return "pet-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login.jsp";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petDAO.get(id);
        if (pet == null || (!"ADMIN".equals(usuario.getPapel()) && !usuario.getId().equals(pet.getUsuario().getId()))) {
            return "redirect:/pets";
        }
        petDAO.delete(pet);
        return "redirect:/pets";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/login.jsp";
        }
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String raca = request.getParameter("raca");
        String idadeParam = request.getParameter("idade");
        String porte = request.getParameter("porte");
        String castradoParam = request.getParameter("castrado");
        String descricao = request.getParameter("descricao");

        if (nome == null || raca == null || idadeParam == null || porte == null || nome.isBlank() || raca.isBlank()
                || idadeParam.isBlank() || porte.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios.");
            return "pet-form";
        }

        Integer idade;
        try {
            idade = Integer.parseInt(idadeParam);
        } catch (NumberFormatException e) {
            model.addAttribute("erro", "Idade deve ser um número inteiro.");
            return "pet-form";
        }

        Boolean castrado = castradoParam != null;
        if (idParam == null || idParam.isBlank()) {
            Pet pet = new Pet(nome, raca, idade, porte, castrado, descricao, usuario);
            petDAO.insert(pet);
        } else {
            Long id = Long.parseLong(idParam);
            Pet existing = petDAO.get(id);
            if (existing == null || (!"ADMIN".equals(usuario.getPapel()) && !usuario.getId().equals(existing.getUsuario().getId()))) {
                return "redirect:/pets";
            }
            Pet pet = new Pet(id, nome, raca, idade, porte, castrado, descricao, existing.getUsuario());
            petDAO.update(pet);
        }
        return "redirect:/pets";
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
