package com.btg.pactual.domain.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(collection = "transacciones")
public class Transaccion {
    private String id;
    private String idCliente;
    private String idFondo;
    private String tipo; // apertura o cancelación
    private Date fecha;
    private BigDecimal monto;
}
