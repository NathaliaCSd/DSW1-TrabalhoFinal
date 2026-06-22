package br.ufscar.dc.dsw.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_papeis", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "papeis")
    private List<String> papeis = new ArrayList<>();

    @OneToMany(mappedBy = "dono")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Casa> casas = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(String nome, String login, String senha, String papeis) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.papeis = Arrays.asList(papeis.split(","));
    }

    public Usuario(Long id, String nome, String login, String senha, String papeis) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.papeis = Arrays.asList(papeis.split(","));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<String> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<String> papeis) {
        this.papeis = papeis;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Casa> getCasas() {
        return casas;
    }

    public void setCasas(List<Casa> casas) {
        this.casas = casas;
    }

    // métodos auxiliares
    public boolean isDonoDePet() {
        return papeis.contains("DONO_PET");
    }

    public boolean isDonoDeHospedagem() {
        return papeis.contains("DONO_HOSPEDAGEM");
    }

    public boolean isAdmin() {
        return papeis.contains("ADMIN");
    }
}
