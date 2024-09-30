package com.btg.pactual.dto;

import lombok.Data;

@Data
public class ClienteRequest {
    private String nombre;
    private String apellidos;
    private String ciudad;
    private String telefono;  // Nuevo campo para el número de teléfono
    private String preferenciaNotificacion;  // "email" o "sms"
}
