package com.btg.pactual.adapters.rest;

import com.btg.pactual.application.FondoService;
import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.ports.ClienteRepository;
import com.btg.pactual.dto.CancelacionRequest;
import com.btg.pactual.dto.ClienteRequest;
import com.btg.pactual.dto.SuscripcionRequest;
import com.btg.pactual.dto.TransaccionResponse;
import com.btg.pactual.exception.ClienteNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/fondos")
public class FondoController {

    private final FondoService fondoService;
    private final ClienteRepository clienteRepository;

    public FondoController(FondoService fondoService, ClienteRepository clienteRepository) {
        this.fondoService = fondoService;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping("/suscribir")
    public ResponseEntity<String> suscribirAFondo(@Valid @RequestBody SuscripcionRequest request) {
        System.out.println("Intentando suscribir al cliente con ID: " + request.getClienteId() + " al fondo con ID: " + request.getFondoId());
        try {
            fondoService.suscribirAFondo(request.getClienteId(), request.getFondoId());
            return ResponseEntity.ok("Suscripción al fondo realizada exitosamente");
        } catch (ClienteNotFoundException e) {
            System.out.println("Error: Cliente no encontrado.");
            return ResponseEntity.badRequest().body("Cliente no encontrado.");
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancelar")
    public ResponseEntity<String> cancelarFondo(@Valid @RequestBody CancelacionRequest request) {
        try {
            fondoService.cancelarFondo(request.getClienteId(), request.getFondoId());
            return ResponseEntity.ok("Cancelación del fondo realizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/historial/{clienteId}")
    public ResponseEntity<List<TransaccionResponse>> obtenerHistorial(@PathVariable String clienteId) {
        try {
            List<TransaccionResponse> historial = fondoService.obtenerHistorial(clienteId).stream()
                    .map(transaccion -> new TransaccionResponse(
                            transaccion.getId(),
                            transaccion.getIdCliente(),
                            transaccion.getIdFondo(),
                            transaccion.getTipo(),
                            transaccion.getFecha(),
                            transaccion.getMonto()
                    ))
                    .toList();
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/clientes")
    public ResponseEntity<String> crearCliente(@RequestBody ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setCiudad(request.getCiudad());
        cliente.setTelefono(request.getTelefono());
        cliente.setPreferenciaNotificacion(request.getPreferenciaNotificacion());

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Cliente creado exitosamente con un saldo inicial de COP $500.000.");
    }

}
