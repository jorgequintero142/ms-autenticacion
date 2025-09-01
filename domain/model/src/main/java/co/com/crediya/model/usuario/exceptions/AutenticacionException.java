package co.com.crediya.model.usuario.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AutenticacionException extends Exception {
    protected final String error;
    protected final int httpCodigo;

    /**
     * Se definen estas constantes para que a nivel de
     * dominio no se importe las clases del paquete
     * http de spring
     */
    static final int HTTP_NOT_FOUND = 404;
    static final int HTTP_BAD_REQUEST = 400;

    AutenticacionException(String error, String mensaje, int httpCodigo) {
        super(mensaje);
        this.error = error;
        this.httpCodigo = httpCodigo;
    }
}
