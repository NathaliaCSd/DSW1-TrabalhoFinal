package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.util.List;

import br.ufscar.dc.dsw.dao.CasaDAO;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/casas/*"})
public class CasaController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CasaDAO casaDAO;

    @Override
    public void init() {
        casaDAO = new CasaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null || "/".equals(action)) {
            listar(request, response);
            return;
        }

        switch (action) {
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
            response.sendRedirect(request.getContextPath() + "/casas");
            return;
        }

        switch (action) {
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        List<Casa> lista = casaDAO.getAll();
        request.setAttribute("casas", lista);
        request.getRequestDispatcher("/casas.jsp").forward(request, response);
    }

    private void solicitarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/casas");
            return;
        }
        request.getRequestDispatcher("/casa-form.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/casas");
            return;
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = casaDAO.get(id);
        request.setAttribute("casa", casa);
        request.getRequestDispatcher("/casa-form.jsp").forward(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/casas");
            return;
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Casa casa = new Casa(id);
        casaDAO.delete(casa);
        response.sendRedirect(request.getContextPath() + "/casas");
    }

    private void salvar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null || !"ADMIN".equals(usuario.getPapel())) {
            response.sendRedirect(request.getContextPath() + "/casas");
            return;
        }
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String endereco = request.getParameter("endereco");
        String descricao = request.getParameter("descricao");
        String diariaParam = request.getParameter("diaria");
        String capacidadeParam = request.getParameter("capacidade");

        Float diaria = 0f;
        Integer capacidade = 0;
        try {
            diaria = Float.parseFloat(diariaParam);
            capacidade = Integer.parseInt(capacidadeParam);
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "Preencha corretamente diária e capacidade.");
            request.getRequestDispatcher("/casa-form.jsp").forward(request, response);
            return;
        }

        Casa casa;
        if (idParam == null || idParam.isBlank()) {
            casa = new Casa(nome, endereco, descricao, diaria, capacidade);
            casaDAO.insert(casa);
        } else {
            Long id = Long.parseLong(idParam);
            casa = new Casa(id, nome, endereco, descricao, diaria, capacidade);
            casaDAO.update(casa);
        }
        response.sendRedirect(request.getContextPath() + "/casas");
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
