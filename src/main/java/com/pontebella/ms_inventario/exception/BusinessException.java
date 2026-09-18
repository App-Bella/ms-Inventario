package com.pontebella.ms_inventario.exception;

// Para violaciones de reglas de negocio: stock insuficiente, insumo
// inactivo, cantidad inválida, etc. (no es un 404, es un 400/409).
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}