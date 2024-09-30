package com.btg.pactual.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransaccionResponse {
    private String id;
    private String idCliente;
    private String idFondo;
    private String tipo;
    private Date fecha;
    private BigDecimal monto;

    public TransaccionResponse(String id, String idCliente, String idFondo, String tipo, Date fecha, BigDecimal monto) {
        this.id = id;
        this.idCliente = idCliente;
        this.idFondo = idFondo;
        this.tipo = tipo;
        this.fecha = fecha;
        this.monto = monto;
    }
}
