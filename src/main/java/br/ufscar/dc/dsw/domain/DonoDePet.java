package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DonoDePet")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class DonoDePet extends Usuario {

    @Column(unique = true, length = 11)
    private String cpf;

    @Column(length = 15)
    private String telefone;

    // "M" ou "F"
    @Column(length = 1)
    private String sexo;

    private LocalDate dataNascimento;

    public DonoDePet() {
        super();
        getPapeis().add("DONO_PET");
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}