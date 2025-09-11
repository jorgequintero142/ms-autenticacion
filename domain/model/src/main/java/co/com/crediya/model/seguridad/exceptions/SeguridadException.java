package co.com.crediya.model.seguridad.exceptions;

import co.com.crediya.model.exceptions.AutenticacionException;

public class SeguridadException extends AutenticacionException {
    static final String MENSAJE = "Error de autorización/autenticacion";

    public SeguridadException(String error) {
        super(error, SeguridadException.MENSAJE, AutenticacionException.ESTADO_AUTENTICACION);
    }
}