package com.btg.pactual.application;

import com.btg.pactual.domain.model.Cliente;
import com.btg.pactual.domain.model.Fondo;
import com.btg.pactual.domain.model.Transaccion;
import com.btg.pactual.domain.ports.ClienteRepository;
import com.btg.pactual.domain.ports.FondoRepository;
import com.btg.pactual.domain.ports.NotificationPort;
import com.btg.pactual.domain.ports.TransaccionRepository;
import com.btg.pactual.exception.ClienteNotFoundException;
import com.btg.pactual.exception.FondoNotFoundException;
import com.btg.pactual.exception.InsufficientFundsException;
import com.btg.pactual.infrastructure.notification.EmailNotificationService;
import com.btg.pactual.infrastructure.notification.SMSNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FondoServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FondoRepository fondoRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private SMSNotificationService smsNotificationService;

    @InjectMocks
    private FondoService fondoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializar los mocks
    }

    // Caso 1: Suscribir a un fondo con éxito y enviar notificación por email
    @Test
    void testSuscribirAFondoSuccessWithEmailNotification() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setSaldo(BigDecimal.valueOf(1000000)); // Cliente con suficiente saldo
        cliente.setPreferenciaNotificacion("email");

        Fondo fondo = new Fondo();
        fondo.setId("f1");
        fondo.setMontoMinimo(BigDecimal.valueOf(500000)); // Monto mínimo

        when(clienteRepository.findById("123")).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondo));

        // Acción
        fondoService.suscribirAFondo("123", "f1");

        // Verificación de interacciones
        verify(clienteRepository, times(1)).save(cliente);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(emailNotificationService, times(1)).enviarNotificacion(cliente, fondo, "apertura");
        verify(smsNotificationService, never()).enviarNotificacion(any(), any(), any());

        // Verificación del saldo del cliente
        assertEquals(BigDecimal.valueOf(500000), cliente.getSaldo());
    }

    // Caso 2: Suscribir a un fondo con éxito y enviar notificación por SMS
    @Test
    void testSuscribirAFondoSuccessWithSmsNotification() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setSaldo(BigDecimal.valueOf(1000000)); // Cliente con suficiente saldo
        cliente.setPreferenciaNotificacion("sms");

        Fondo fondo = new Fondo();
        fondo.setId("f1");
        fondo.setMontoMinimo(BigDecimal.valueOf(500000)); // Monto mínimo

        when(clienteRepository.findById("123")).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondo));

        // Acción
        fondoService.suscribirAFondo("123", "f1");

        // Verificación de interacciones
        verify(clienteRepository, times(1)).save(cliente);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(smsNotificationService, times(1)).enviarNotificacion(cliente, fondo, "apertura");
        verify(emailNotificationService, never()).enviarNotificacion(any(), any(), any());

        // Verificación del saldo del cliente
        assertEquals(BigDecimal.valueOf(500000), cliente.getSaldo());
    }

    // Caso 3: Suscribir a un fondo con saldo insuficiente
    @Test
    void testSuscribirAFondoInsufficientFunds() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setSaldo(BigDecimal.valueOf(300000)); // Cliente con saldo insuficiente
        cliente.setPreferenciaNotificacion("email");

        Fondo fondo = new Fondo();
        fondo.setId("f1");
        fondo.setMontoMinimo(BigDecimal.valueOf(500000)); // Monto mínimo

        when(clienteRepository.findById("123")).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondo));

        // Acción y verificación de excepción
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            fondoService.suscribirAFondo("123", "f1");
        });

        assertEquals("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre(), exception.getMessage());

        // Verificación de que no se haya realizado la transacción ni enviado notificación
        verify(transaccionRepository, never()).save(any(Transaccion.class));
        verify(emailNotificationService, never()).enviarNotificacion(any(), any(), any());
        verify(smsNotificationService, never()).enviarNotificacion(any(), any(), any());
    }

    // Caso 4: Suscribir a un fondo con cliente no encontrado
    @Test
    void testSuscribirAFondoClienteNotFound() {
        // Datos de prueba
        when(clienteRepository.findById("123")).thenReturn(Optional.empty());

        // Acción y verificación de excepción
        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class, () -> {
            fondoService.suscribirAFondo("123", "f1");
        });

        assertEquals("Cliente no encontrado.", exception.getMessage());

        // Verificación de que no se haya realizado la transacción ni enviado notificación
        verify(transaccionRepository, never()).save(any(Transaccion.class));
        verify(emailNotificationService, never()).enviarNotificacion(any(), any(), any());
        verify(smsNotificationService, never()).enviarNotificacion(any(), any(), any());
    }

    // Caso 5: Suscribir a un fondo con fondo no encontrado
    @Test
    void testSuscribirAFondoFondoNotFound() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setSaldo(BigDecimal.valueOf(1000000)); // Cliente con suficiente saldo
        cliente.setPreferenciaNotificacion("email");

        when(clienteRepository.findById("123")).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById("f1")).thenReturn(Optional.empty());

        // Acción y verificación de excepción
        FondoNotFoundException exception = assertThrows(FondoNotFoundException.class, () -> {
            fondoService.suscribirAFondo("123", "f1");
        });

        assertEquals("Fondo no encontrado.", exception.getMessage());

        // Verificación de que no se haya realizado la transacción ni enviado notificación
        verify(transaccionRepository, never()).save(any(Transaccion.class));
        verify(emailNotificationService, never()).enviarNotificacion(any(), any(), any());
        verify(smsNotificationService, never()).enviarNotificacion(any(), any(), any());
    }

    // Caso 6: Cancelar un fondo con éxito y enviar notificación por email
    @Test
    void testCancelarFondoSuccessWithEmailNotification() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setSaldo(BigDecimal.valueOf(500000)); // Cliente con saldo actual
        cliente.setPreferenciaNotificacion("email");

        Fondo fondo = new Fondo();
        fondo.setId("f1");
        fondo.setMontoMinimo(BigDecimal.valueOf(500000)); // Monto mínimo

        when(clienteRepository.findById("123")).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondo));

        // Acción
        fondoService.cancelarFondo("123", "f1");

        // Verificación de interacciones
        verify(clienteRepository, times(1)).save(cliente);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(emailNotificationService, times(1)).enviarNotificacion(cliente, fondo, "cancelacion");
        verify(smsNotificationService, never()).enviarNotificacion(any(), any(), any());

        // Verificación del saldo del cliente tras la cancelación
        assertEquals(BigDecimal.valueOf(1000000), cliente.getSaldo());
    }

    // Caso 7: Obtener historial de transacciones de un cliente
    @Test
    void testObtenerHistorial() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId("123");

        Transaccion transaccion1 = new Transaccion();
        transaccion1.setId("t1");
        transaccion1.setIdCliente("123");
        transaccion1.setTipo("apertura");

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId("t2");
        transaccion2.setIdCliente("123");
        transaccion2.setTipo("cancelacion");

        when(transaccionRepository.findByIdCliente("123")).thenReturn(List.of(transaccion1, transaccion2));

        // Acción
        var historial = fondoService.obtenerHistorial("123");

        // Verificación
        assertEquals(2, historial.size());
        assertEquals("t1", historial.get(0).getId());
        assertEquals("t2", historial.get(1).getId());
    }
}
