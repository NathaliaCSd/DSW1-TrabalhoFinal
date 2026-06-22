package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.PetRepository;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        Pet petLogado = getPetLogado(request);
        List<Pet> lista;
        if (usuario != null && !usuario.isAdmin()) {
            lista = petRepository.findByDono(usuario);
        } else {
            lista = petRepository.findAll();
        }
        model.addAttribute("pets", lista);
        model.addAttribute("petLogado", petLogado);
        model.addAttribute("usuarioLogado", usuario);
        return "pets";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("pet", new Pet());
        return "pet-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petRepository.findById(id).orElse(null);
        Usuario usuario = getUsuarioLogado(request);
        Pet petLogado = getPetLogado(request);
        if (pet == null || (usuario == null || !usuario.isAdmin())
                && (petLogado == null || !petLogado.getId().equals(pet.getId()))) {
            return "redirect:/pets";
        }
        model.addAttribute("pet", pet);
        model.addAttribute("petLogado", petLogado);
        model.addAttribute("usuarioLogado", usuario);
        return "pet-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petRepository.findById(id).orElse(null);
        Usuario usuario = getUsuarioLogado(request);
        Pet petLogado = getPetLogado(request);
        if (pet == null || ((usuario == null || !usuario.isAdmin())
                && (petLogado == null || !petLogado.getId().equals(pet.getId())))) {
            return "redirect:/pets";
        }
        petRepository.delete(pet);
        if (petLogado != null && petLogado.getId().equals(pet.getId())) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute("petLogado");
            }
        }
        return "redirect:/pets";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String raca = request.getParameter("raca");
        String idadeParam = request.getParameter("idade");
        String porte = request.getParameter("porte");
        String castradoParam = request.getParameter("castrado");
        String descricao = request.getParameter("descricao");
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        if (nome == null || raca == null || idadeParam == null || porte == null || nome.isBlank() || raca.isBlank()
                || idadeParam.isBlank() || porte.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios.");
            Pet petTemp = new Pet();
            petTemp.setNome(nome);
            petTemp.setRaca(raca);
            petTemp.setPorte(porte);
            model.addAttribute("pet", petTemp);
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

        Pet pet;
        if (idParam == null || idParam.isBlank()) {
            pet = new Pet(nome, raca, idade, porte, castrado, descricao);
            pet.setDono(usuario);
        } else {
            Long id = Long.parseLong(idParam);
            pet = petRepository.findById(id).orElse(null);
            if (pet == null) {
                return "redirect:/pets";
            }
            pet.setNome(nome);
            pet.setRaca(raca);
            pet.setIdade(idade);
            pet.setPorte(porte);
            pet.setCastrado(castrado);
            pet.setDescricao(descricao);
            if (pet.getDono() == null) {
                pet.setDono(usuario);
            }
        }
        pet = petRepository.save(pet);
        if (idParam == null || idParam.isBlank()) {
            HttpSession session = request.getSession();
            session.setAttribute("petLogado", pet);
        } else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Pet petLogado = (Pet) session.getAttribute("petLogado");
                if (petLogado != null && petLogado.getId().equals(pet.getId())) {
                    session.setAttribute("petLogado", pet);
                }
            }
        }
        return "redirect:/pets";
    }

    @GetMapping("/selecionar")
    public String selecionar(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petRepository.findById(id).orElse(null);
        // só seleciona se o pet pertence ao usuário ou se é ADMIN
        if (pet != null && (usuario.isAdmin() || pet.getDono().getId().equals(usuario.getId()))) {
            HttpSession session = request.getSession();
            session.setAttribute("petLogado", pet);
        }
        return "redirect:/pets";
    }

    @GetMapping("/sair")
    public String sair(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("petLogado");
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

    private Pet getPetLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Pet) session.getAttribute("petLogado");
    }
}
