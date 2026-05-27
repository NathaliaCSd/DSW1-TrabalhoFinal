package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.util.List;

import br.ufscar.dc.dsw.dao.UsuarioDAO;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/usuario/*"})
public class UsuarioController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null || "/".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        switch (action) {
            case "/cadastro":
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                break;
            case "/logout":
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
            case "/lista":
                listar(request, response);
                break;
            case "/novo":
                solicitarFormulario(request, response);
                break;
            case "/edicao":
                editar(request, response);
                break;
            case "/excluir":
                excluir(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        switch (action) {
            case "/cadastro":
                cadastrar(request, response);
                break;
            case "/login":
                login(request, response);
                break;
            case "/salvar":
                salvar(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        List<Usuario> lista = usuarioDAO.getAll();
        request.setAttribute("usuarios", lista);
        request.getRequestDispatcher("/usuarios.jsp").forward(request, response);
    }

    private void solicitarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        request.getRequestDispatcher("/usuario-form.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Usuario usuarioEdicao = usuarioDAO.get(id);
        request.setAttribute("usuario", usuarioEdicao);
        request.getRequestDispatcher("/usuario-form.jsp").forward(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Usuario usuarioExcluir = new Usuario(id);
        usuarioDAO.delete(usuarioExcluir);
        response.sendRedirect(request.getContextPath() + "/usuario/lista");
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        if (nome == null || login == null || senha == null || nome.isBlank() || login.isBlank() || senha.isBlank()) {
            request.setAttribute("erro", "Preencha todos os campos para criar a conta.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (usuarioDAO.getByLogin(login) != null) {
            request.setAttribute("erro", "O login já existe. Escolha outro nome de usuário.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        Usuario usuario = new Usuario(nome, login, senha, "USER");
        usuarioDAO.insert(usuario);
        request.setAttribute("mensagem", "Conta criada com sucesso. Faça login abaixo.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        Usuario usuario = usuarioDAO.authenticate(login, senha);
        if (usuario == null) {
            request.setAttribute("erro", "Login ou senha incorretos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("usuarioLogado", usuario);
        response.sendRedirect(request.getContextPath() + "/casas");
    }

    private void salvar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        String papel = request.getParameter("papel");

        if (nome == null || login == null || papel == null || nome.isBlank() || login.isBlank() || papel.isBlank()) {
            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/usuario-form.jsp").forward(request, response);
            return;
        }

        if (idParam == null || idParam.isBlank()) {
            if (senha == null || senha.isBlank()) {
                request.setAttribute("erro", "Senha é obrigatória para novo usuário.");
                request.getRequestDispatcher("/usuario-form.jsp").forward(request, response);
                return;
            }
            if (usuarioDAO.getByLogin(login) != null) {
                request.setAttribute("erro", "O login já existe. Escolha outro nome de usuário.");
                request.getRequestDispatcher("/usuario-form.jsp").forward(request, response);
                return;
            }
            Usuario novo = new Usuario(nome, login, senha, papel);
            usuarioDAO.insert(novo);
        } else {
            Long id = Long.parseLong(idParam);
            Usuario existente = usuarioDAO.get(id);
            if (existente == null) {
                response.sendRedirect(request.getContextPath() + "/usuario/lista");
                return;
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
        response.sendRedirect(request.getContextPath() + "/usuario/lista");
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
