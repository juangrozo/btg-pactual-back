package com.btg.pactual.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "fondos")
public class Fondo {
    @Id
    private String id;
    private String nombre;
    private BigDecimal montoMinimo;
    private String categoria;
}
