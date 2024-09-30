package com.btg.pactual.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException() {
        super("Cliente no encontrado.");
    }
}
