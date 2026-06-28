package br.ufscar.dc.dsw.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import br.ufscar.dc.dsw.dao.UsuarioRepository;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Anfitriao;
import br.ufscar.dc.dsw.domain.DonoDePet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        if (request.getParameter("erro") != null) {
            model.addAttribute("erro", "Login ou senha incorretos.");
        }
        return "login";
    }

    @GetMapping("/perfil")
    public String perfil(Authentication auth, Model model) {
        Usuario usuario = usuarioRepository.findByLogin(auth.getName());
        model.addAttribute("usuario", usuario);
        return "usuario-form";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios";
    }

    @GetMapping("/novo")
    public String solicitarFormulario() {
        return "usuario-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Long id = Long.parseLong(request.getParameter("id"));
        model.addAttribute("usuario", usuarioRepository.findById(id).orElse(null));
        return "usuario-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        usuarioRepository.deleteById(id);
        return "redirect:/usuario/lista";
    }

    

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Authentication auth, Model model) {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        String[] papeis = request.getParameterValues("papeis");

        if (nome == null || nome.isBlank() || login == null || login.isBlank()) {
            model.addAttribute("erro", "Nome e login são obrigatórios.");
            return "usuario-form";
        }

        // ---- CRIAÇÃO (idParam vazio — só ADMIN chega aqui via SecurityConfig) ----
        if (idParam == null || idParam.isBlank()) {
            if (senha == null || senha.isBlank()) {
                model.addAttribute("erro", "Senha é obrigatória.");
                return "usuario-form";
            }
            if (papeis == null || papeis.length == 0) {
                model.addAttribute("erro", "Selecione pelo menos um perfil.");
                return "usuario-form";
            }
            if (usuarioRepository.findByLogin(login) != null) {
                model.addAttribute("erro", "Login já existe.");
                return "usuario-form";
            }

            List<String> listaPapeis = Arrays.asList(papeis);
            Usuario novo;

            if (listaPapeis.contains("DONO_PET") && !listaPapeis.contains("DONO_HOSPEDAGEM")) {
                // Tutor — campos extras
                DonoDePet tutor = new DonoDePet();
                tutor.setCpf(request.getParameter("cpf"));
                tutor.setTelefone(request.getParameter("telefone"));
                tutor.setSexo(request.getParameter("sexo"));
                String dataNasc = request.getParameter("dataNascimento");
                if (dataNasc != null && !dataNasc.isBlank()) {
                    tutor.setDataNascimento(LocalDate.parse(dataNasc));
                }
                novo = tutor;

            } else if (listaPapeis.contains("DONO_HOSPEDAGEM") && !listaPapeis.contains("DONO_PET")) {
                // Anfitrião — campos extras
                Anfitriao anf = new Anfitriao();
                anf.setCnpj(request.getParameter("cnpj"));
                anf.setDescricaoServico(request.getParameter("descricaoServico"));
                novo = anf;

            } else {
                // ADMIN ou perfil misto — usa Usuario base
                novo = new Usuario();
            }

            novo.setNome(nome);
            novo.setLogin(login);
            novo.setSenha(passwordEncoder.encode(senha));
            novo.setPapeis(listaPapeis);
            usuarioRepository.save(novo);
            return "redirect:/usuario/lista";
        }

        // ---- EDIÇÃO ----
        Long id = Long.parseLong(idParam);
        Usuario usuarioSalvo = usuarioRepository.findById(id).orElse(null);
        if (usuarioSalvo == null)
            return "redirect:/usuario/lista";

        // verifica conflito de login
        Usuario existing = usuarioRepository.findByLogin(login);
        if (existing != null && !existing.getId().equals(usuarioSalvo.getId())) {
            model.addAttribute("erro", "Login já existe.");
            model.addAttribute("usuario", usuarioSalvo);
            return "usuario-form";
        }

        usuarioSalvo.setNome(nome);
        usuarioSalvo.setLogin(login);
        if (senha != null && !senha.isBlank()) {
            usuarioSalvo.setSenha(passwordEncoder.encode(senha));
        }

        // admin pode alterar papéis
        Usuario logado = usuarioRepository.findByLogin(auth.getName());
        if (logado.isAdmin() && papeis != null) {
            usuarioSalvo.setPapeis(Arrays.asList(papeis));
        }

        // campos extras por tipo
        if (usuarioSalvo instanceof DonoDePet tutor) {
            if (request.getParameter("cpf") != null)
                tutor.setCpf(request.getParameter("cpf"));
            if (request.getParameter("telefone") != null)
                tutor.setTelefone(request.getParameter("telefone"));
            if (request.getParameter("sexo") != null)
                tutor.setSexo(request.getParameter("sexo"));
            String dataNasc = request.getParameter("dataNascimento");
            if (dataNasc != null && !dataNasc.isBlank())
                tutor.setDataNascimento(LocalDate.parse(dataNasc));
        } else if (usuarioSalvo instanceof Anfitriao anf) {
            if (request.getParameter("cnpj") != null)
                anf.setCnpj(request.getParameter("cnpj"));
            if (request.getParameter("descricaoServico") != null)
                anf.setDescricaoServico(request.getParameter("descricaoServico"));
        }

        usuarioRepository.save(usuarioSalvo);

        // se editou o próprio perfil, atualiza sessão
        if (usuarioSalvo.getLogin().equals(auth.getName())) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.setAttribute("usuarioLogado", usuarioSalvo);
        }
        if (logado.isAdmin())
            return "redirect:/usuario/lista";
        return "redirect:/";
    }
}
