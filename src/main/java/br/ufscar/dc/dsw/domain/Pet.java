package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String raca;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false)
    private String porte;

    @Column(nullable = false)
    private boolean castrado;

    @Column(length = 512)
    private String descricao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dono_id", nullable = false)
    private Usuario dono;

    public Pet() {
    }

    public Pet(Long id) {
        this.id = id;
    }

    public Pet(String nome, String raca, Integer idade, String porte, Boolean castrado, String descricao) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.porte = porte;
        this.castrado = castrado;
        this.descricao = descricao;
    }

    public Pet(Long id, String nome, String raca, Integer idade, String porte, Boolean castrado, String descricao) {
        this(nome, raca, idade, porte, castrado, descricao);
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

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public Boolean getCastrado() {
        return castrado;
    }

    public void setCastrado(Boolean castrado) {
        this.castrado = castrado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }
}
