package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;

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

    @Column
    private String cidade;

    @Column
    private String estado;

    @Column
    private String pais;

    @Column
    private LocalDate dataDisponibilidade;

    @Column
    private Integer duracaoMinima;

    @Column
    private Integer duracaoMaxima;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Casa() {
    }

    public Casa(Long id) {
        this.id = id;
    }

    public Casa(String nome, String endereco, String descricao, Float diaria, Integer capacidade, String cidade, String estado, String pais, LocalDate dataDisponibilidade, Integer duracaoMinima, Integer duracaoMaxima) {
        this.nome = nome;
        this.endereco = endereco;
        this.descricao = descricao;
        this.diaria = diaria;
        this.capacidade = capacidade;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.dataDisponibilidade = dataDisponibilidade;
        this.duracaoMinima = duracaoMinima;
        this.duracaoMaxima = duracaoMaxima;
    }

    public Casa(Long id, String nome, String endereco, String descricao, Float diaria, Integer capacidade, String cidade, String estado, String pais, LocalDate dataDisponibilidade, Integer duracaoMinima, Integer duracaoMaxima) {
        this(nome, endereco, descricao, diaria, capacidade, cidade,  estado,  pais,  dataDisponibilidade,  duracaoMinima,  duracaoMaxima);
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public java.time.LocalDate getDataDisponibilidade() {
        return dataDisponibilidade;
    }

    public void setDataDisponibilidade(java.time.LocalDate dataDisponibilidade) {
        this.dataDisponibilidade = dataDisponibilidade;
    }

    public Integer getDuracaoMinima() {
        return duracaoMinima;
    }

    public void setDuracaoMinima(Integer duracaoMinima) {
        this.duracaoMinima = duracaoMinima;
    }

    public Integer getDuracaoMaxima() {
        return duracaoMaxima;
    }

    public void setDuracaoMaxima(Integer duracaoMaxima) {
        this.duracaoMaxima = duracaoMaxima;
    }
}
