package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Anfitriao")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Anfitriao extends Usuario {

    @Column(unique = true, length = 14)
    private String cnpj;

    @Column(length = 512)
    private String descricaoServico;

    public Anfitriao() {
        super();
        getPapeis().add("DONO_HOSPEDAGEM");
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getDescricaoServico() { return descricaoServico; }
    public void setDescricaoServico(String descricaoServico) { this.descricaoServico = descricaoServico; }
}
