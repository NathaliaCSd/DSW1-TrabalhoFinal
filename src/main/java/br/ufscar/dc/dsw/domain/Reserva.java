package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;

public class Reserva {

    private Long id;
    private Pet pet;
    private Casa casa;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Float total;

    public Reserva() {
    }

    public Reserva(Long id) {
        this.id = id;
    }

    public Reserva(Pet pet, Casa casa, LocalDate dataInicio, LocalDate dataFim, Float total) {
        this.pet = pet;
        this.casa = casa;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.total = total;
    }

    public Reserva(Long id, Pet pet, Casa casa, LocalDate dataInicio, LocalDate dataFim, Float total) {
        this(pet, casa, dataInicio, dataFim, total);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
