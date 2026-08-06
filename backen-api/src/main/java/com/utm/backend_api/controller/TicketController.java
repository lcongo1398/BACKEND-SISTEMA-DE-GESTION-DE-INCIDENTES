package com.utm.backend_api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.utm.backend_api.entity.Ticket;
import com.utm.backend_api.enums.Estado;
import com.utm.backend_api.repository.TicketRepository;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketRepository repositorio;

    // Constructor para inyectar el repositorio
    public TicketController(TicketRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Obtener todos
    @GetMapping
    public List<Ticket> listarTodos() {
        return repositorio.findAll();
    }

    // Obtener uno por id
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> buscarPorId(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo
    @PostMapping
    public Ticket crearNuevo(@RequestBody Ticket ticket) {
        return repositorio.save(ticket);
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        Optional<Ticket> ticketOpt = repositorio.findById(id);
        
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ticket ticketExistente = ticketOpt.get();

        if (datos.containsKey("estado")) {
            String valorEstado = datos.get("estado").toString().trim();
            ticketExistente.setEstado(Estado.valueOf(valorEstado));
        }

        Ticket guardado = repositorio.save(ticketExistente);
        return ResponseEntity.ok(guardado);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarTicket(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}