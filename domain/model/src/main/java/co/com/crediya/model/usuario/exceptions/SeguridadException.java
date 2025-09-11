package co.com.crediya.model.usuario.exceptions;

public class SeguridadException extends AutenticacionException {
    static final String MENSAJE = "Error de autorización/autenticacion";

    public SeguridadException(String error) {
        super(error, SeguridadException.MENSAJE, AutenticacionException.ESTADO_AUTENTICACION);
    }
}