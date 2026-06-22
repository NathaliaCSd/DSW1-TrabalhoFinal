package br.ufscar.dc.dsw.services.spec;

import java.util.List;
import br.ufscar.dc.dsw.domain.Reserva;

public interface IReservaService {
    Reserva buscarPorId(Long id);
    List<Reserva> buscarTodos();
    List<Reserva> buscarPorPetId(Long petId);
    void salvar(Reserva reserva);
    void excluir(Long id);
}