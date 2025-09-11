package co.com.crediya.model.exceptions;

import lombok.Getter;

@Getter
public abstract class AutenticacionException extends Exception {
    protected final String error;
    protected final int codigoEstado;

    protected static final int ESTADO_NO_ENCONTRADO = 404;
    protected static final int ESTADO_ERROR_PARAMETRO = 400;
    protected static final int ESTADO_AUTENTICACION = 401;

    protected AutenticacionException(String error, String mensaje, int codigoEstado) {
        super(mensaje);
        this.error = error;
        this.codigoEstado = codigoEstado;
    }
}
