package com.btg.pactual.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "clientes")
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private String apellidos;
    private String ciudad;
    private String telefono;  // Nuevo campo para el número de teléfono
    private String correo;
    private String preferenciaNotificacion;  // "email" o "sms"
    private BigDecimal saldo = BigDecimal.valueOf(500000);  // Saldo inicial de 500,000 COP
}
