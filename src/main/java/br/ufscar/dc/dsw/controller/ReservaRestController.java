package br.ufscar.dc.dsw.controller;


import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw.dao.CasaRepository;
import br.ufscar.dc.dsw.dao.PetRepository;
import br.ufscar.dc.dsw.domain.Casa;
import br.ufscar.dc.dsw.domain.Pet;
import br.ufscar.dc.dsw.domain.Reserva;
import br.ufscar.dc.dsw.services.spec.IReservaService;

@RestController
public class ReservaRestController {

    @Autowired
    private IReservaService service;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CasaRepository casaRepository;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void parse(Reserva reserva, JSONObject json) {

        // id (para PUT)
        Object id = json.get("id");
        if (id != null) {
            if (id instanceof Integer) {
                reserva.setId(((Integer) id).longValue());
            } else {
                reserva.setId((Long) id);
            }
        }

        // datas
        reserva.setDataInicio(LocalDate.parse((String) json.get("dataInicio")));
        reserva.setDataFim(LocalDate.parse((String) json.get("dataFim")));

        // total calculado automaticamente
        long dias = ChronoUnit.DAYS.between(reserva.getDataInicio(), reserva.getDataFim());

        // pet (busca pelo id vindo no JSON)
        Map<String, Object> petMap = (Map<String, Object>) json.get("pet");
        Object petId = petMap.get("id");
        Long petIdLong = petId instanceof Integer ? ((Integer) petId).longValue() : (Long) petId;
        Pet pet = petRepository.findById(petIdLong).orElse(null);
        reserva.setPet(pet);

        // casa (busca pelo id vindo no JSON)
        Map<String, Object> casaMap = (Map<String, Object>) json.get("casa");
        Object casaId = casaMap.get("id");
        Long casaIdLong = casaId instanceof Integer ? ((Integer) casaId).longValue() : (Long) casaId;
        Casa casa = casaRepository.findById(casaIdLong).orElse(null);
        reserva.setCasa(casa);

        // calcula total com base na diária da casa
        if (casa != null) {
            reserva.setTotal(dias * casa.getDiaria());
        }
    }

    // GET /reservas/api — lista todas
    @GetMapping(path = "/reservas/api")
    public ResponseEntity<List<Reserva>> lista() {
        List<Reserva> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    // GET /reservas/api/{id} — busca por id
    @GetMapping(path = "/reservas/api/{id}")
    public ResponseEntity<Reserva> buscar(@PathVariable("id") long id) {
        Reserva reserva = service.buscarPorId(id);
        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }

    // GET /reservas/api/pet/{petId} — reservas de um pet
    @GetMapping(path = "/reservas/api/pet/{petId}")
    public ResponseEntity<List<Reserva>> listaPorPet(@PathVariable("petId") long petId) {
        List<Reserva> lista = service.buscarPorPetId(petId);
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    // POST /reservas/api — criar nova reserva
    @PostMapping(path = "/reservas/api")
    @ResponseBody
    public ResponseEntity<Reserva> cria(@RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Reserva reserva = new Reserva();
                parse(reserva, json);

                // validação de datas
                if (reserva.getDataFim().isBefore(reserva.getDataInicio()) ||
                    reserva.getDataFim().isEqual(reserva.getDataInicio())) {
                    return ResponseEntity.badRequest().body(null);
                }

                service.salvar(reserva);
                return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    // PUT /reservas/api/{id} — atualizar reserva
    @PutMapping(path = "/reservas/api/{id}")
    public ResponseEntity<Reserva> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Reserva reserva = service.buscarPorId(id);
                if (reserva == null) {
                    return ResponseEntity.notFound().build();
                }
                parse(reserva, json);
                service.salvar(reserva);
                return ResponseEntity.ok(reserva);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    // DELETE /reservas/api/{id} — remover reserva
    @DeleteMapping(path = "/reservas/api/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") long id) {
        Reserva reserva = service.buscarPorId(id);
        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}