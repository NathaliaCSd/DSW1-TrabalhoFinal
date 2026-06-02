package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.dao.UsuarioDAO;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping("/cadastro")
    public String cadastro() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/index.jsp";
    }

    @GetMapping("/lista")
    public String listar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/index.jsp";
        }
        List<Usuario> lista = usuarioDAO.getAll();
        model.addAttribute("usuarios", lista);
        return "usuarios";
    }

    @GetMapping("/novo")
    public String solicitarFormulario(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/index.jsp";
        }
        return "usuario-form";
    }

    @GetMapping("/edicao")
    public String editar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/index.jsp";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Usuario usuarioEdicao = usuarioDAO.get(id);
        model.addAttribute("usuario", usuarioEdicao);
        return "usuario-form";
    }

    @GetMapping("/excluir")
    public String excluir(HttpServletRequest request) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/index.jsp";
        }
        Long id = Long.parseLong(request.getParameter("id"));
        usuarioDAO.delete(new Usuario(id));
        return "redirect:/usuario/lista";
    }

    @PostMapping("/cadastro")
    public String cadastrar(HttpServletRequest request, Model model) {
        String nome = request.getParameter("nome");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        if (nome == null || login == null || senha == null || nome.isBlank() || login.isBlank() || senha.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos para criar a conta.");
            return "register";
        }

        if (usuarioDAO.getByLogin(login) != null) {
            model.addAttribute("erro", "O login já existe. Escolha outro nome de usuário.");
            return "register";
        }

        Usuario usuario = new Usuario(nome, login, senha, "USER");
        usuarioDAO.insert(usuario);
        model.addAttribute("mensagem", "Conta criada com sucesso. Faça login abaixo.");
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        Usuario usuario = usuarioDAO.authenticate(login, senha);
        if (usuario == null) {
            model.addAttribute("erro", "Login ou senha incorretos.");
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("usuarioLogado", usuario);
        return "redirect:/casas";
    }

    @PostMapping("/salvar")
    public String salvar(HttpServletRequest request, Model model) {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            return "redirect:/index.jsp";
        }

        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        String papel = request.getParameter("papel");

        if (nome == null || login == null || papel == null || nome.isBlank() || login.isBlank() || papel.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios.");
            return "usuario-form";
        }

        if (idParam == null || idParam.isBlank()) {
            if (senha == null || senha.isBlank()) {
                model.addAttribute("erro", "Senha é obrigatória para novo usuário.");
                return "usuario-form";
            }
            if (usuarioDAO.getByLogin(login) != null) {
                model.addAttribute("erro", "O login já existe. Escolha outro nome de usuário.");
                return "usuario-form";
            }
            Usuario novo = new Usuario(nome, login, senha, papel);
            usuarioDAO.insert(novo);
        } else {
            Long id = Long.parseLong(idParam);
            Usuario existente = usuarioDAO.get(id);
            if (existente == null) {
                return "redirect:/usuario/lista";
            }
            if (senha == null || senha.isBlank()) {
                senha = existente.getSenha();
            }
            existente.setNome(nome);
            existente.setLogin(login);
            existente.setSenha(senha);
            existente.setPapel(papel);
            usuarioDAO.update(existente);
        }
        return "redirect:/usuario/lista";
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
