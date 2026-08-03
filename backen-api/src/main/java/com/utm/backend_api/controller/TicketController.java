package com.utm.backend_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.utm.backend_api.entity.Ticket;
import com.utm.backend_api.repository.TicketRepository;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketRepository repo;
    public TicketController(TicketRepository repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<List<Ticket>> listarTodos() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> buscarPorId(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@RequestBody Ticket nuevoTicket) {
        Ticket guardado = repo.save(nuevoTicket);
        URI ubicacion = URI.create("/tickets/" + guardado.getId());
        return ResponseEntity.created(ubicacion).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Ticket datos) {
        return repo.findById(id)
                .map(existente -> {
                    existente.setTitulo(datos.getTitulo());
                    existente.setDescripcion(datos.getDescripcion());
                    existente.setCategoria(datos.getCategoria());
                    existente.setPrioridad(datos.getPrioridad());
                    existente.setEstado(datos.getEstado());
                    return ResponseEntity.ok(repo.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}