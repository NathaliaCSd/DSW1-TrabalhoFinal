package br.ufscar.dc.dsw.domain;

public class Pet {

    private Long id;
    private String nome;
    private String raca;
    private Integer idade;
    private String porte;
    private Boolean castrado;
    private String descricao;
    private Usuario usuario;

    public Pet() {
    }

    public Pet(Long id) {
        this.id = id;
    }

    public Pet(String nome, String raca, Integer idade, String porte, Boolean castrado, String descricao, Usuario usuario) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.porte = porte;
        this.castrado = castrado;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    public Pet(Long id, String nome, String raca, Integer idade, String porte, Boolean castrado, String descricao, Usuario usuario) {
        this(nome, raca, idade, porte, castrado, descricao, usuario);
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
