package br.ufscar.dc.dsw.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.ufscar.dc.dsw.dao.ReservaRepository;
import br.ufscar.dc.dsw.domain.Reserva;
import br.ufscar.dc.dsw.services.spec.IReservaService;

@Service
@Transactional(readOnly = false)
public class ReservaService implements IReservaService {

    @Autowired
    ReservaRepository dao;

    @Override
    @Transactional(readOnly = true)
    public Reserva buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> buscarTodos() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> buscarPorPetId(Long petId) {
        return dao.findByPetId(petId);
    }

    @Override
    public void salvar(Reserva reserva) {
        dao.save(reserva);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }
}