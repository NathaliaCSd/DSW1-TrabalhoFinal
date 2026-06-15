package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity // Indica à JPA que esta classe é uma entidade persistente.
@Table(name = "Casa")
public class Casa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(length = 512)
    private String descricao;

    @Column(nullable = false)
    private Float diaria;

    @Column(nullable = false)
    private Integer capacidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Casa() {
    }

    public Casa(Long id) {
        this.id = id;
    }

    public Casa(String nome, String endereco, String descricao, Float diaria, Integer capacidade) {
        this.nome = nome;
        this.endereco = endereco;
        this.descricao = descricao;
        this.diaria = diaria;
        this.capacidade = capacidade;
    }

    public Casa(Long id, String nome, String endereco, String descricao, Float diaria, Integer capacidade) {
        this(nome, endereco, descricao, diaria, capacidade);
        this.id = id;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getDiaria() {
        return diaria;
    }

    public void setDiaria(Float diaria) {
        this.diaria = diaria;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
