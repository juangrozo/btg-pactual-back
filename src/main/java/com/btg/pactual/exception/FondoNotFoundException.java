package com.btg.pactual.exception;

public class FondoNotFoundException extends RuntimeException {
    public FondoNotFoundException() {
        super("Fondo no encontrado.");
    }
}
