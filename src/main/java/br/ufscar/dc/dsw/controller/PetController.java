package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.util.List;

import br.ufscar.dc.dsw.dao.PetDAO;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/pets/*"})
public class PetController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PetDAO petDAO;

    @Override
    public void init() {
        petDAO = new PetDAO();
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
            response.sendRedirect(request.getContextPath() + "/pets");
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
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        List<Pet> lista = "ADMIN".equals(usuario.getPapel()) ? petDAO.getAll() : petDAO.getAllByUsuario(usuario.getId());
        request.setAttribute("pets", lista);
        request.getRequestDispatcher("/pets.jsp").forward(request, response);
    }

    private void solicitarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/pet-form.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petDAO.get(id);
        if (pet == null || (!"ADMIN".equals(usuario.getPapel()) && !usuario.getId().equals(pet.getUsuario().getId()))) {
            response.sendRedirect(request.getContextPath() + "/pets");
            return;
        }
        request.setAttribute("pet", pet);
        request.getRequestDispatcher("/pet-form.jsp").forward(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Long id = Long.parseLong(request.getParameter("id"));
        Pet pet = petDAO.get(id);
        if (pet == null || (!"ADMIN".equals(usuario.getPapel()) && !usuario.getId().equals(pet.getUsuario().getId()))) {
            response.sendRedirect(request.getContextPath() + "/pets");
            return;
        }
        petDAO.delete(pet);
        response.sendRedirect(request.getContextPath() + "/pets");
    }

    private void salvar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = getUsuarioLogado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
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
            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            request.getRequestDispatcher("/pet-form.jsp").forward(request, response);
            return;
        }

        Integer idade;
        try {
            idade = Integer.parseInt(idadeParam);
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "Idade deve ser um número inteiro.");
            request.getRequestDispatcher("/pet-form.jsp").forward(request, response);
            return;
        }

        Boolean castrado = castradoParam != null;
        Pet pet;
        if (idParam == null || idParam.isBlank()) {
            pet = new Pet(nome, raca, idade, porte, castrado, descricao, usuario);
            petDAO.insert(pet);
        } else {
            Long id = Long.parseLong(idParam);
            Pet existing = petDAO.get(id);
            if (existing == null || (!"ADMIN".equals(usuario.getPapel()) && !usuario.getId().equals(existing.getUsuario().getId()))) {
                response.sendRedirect(request.getContextPath() + "/pets");
                return;
            }
            pet = new Pet(id, nome, raca, idade, porte, castrado, descricao, existing.getUsuario());
            petDAO.update(pet);
        }
        response.sendRedirect(request.getContextPath() + "/pets");
    }

    private Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuarioLogado");
    }
}
