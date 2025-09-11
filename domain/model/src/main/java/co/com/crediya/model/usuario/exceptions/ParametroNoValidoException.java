package co.com.crediya.model.usuario.exceptions;


import co.com.crediya.model.exceptions.AutenticacionException;

public class ParametroNoValidoException extends AutenticacionException {
    static final String MENSAJE = "Validación fallida";

    public ParametroNoValidoException(String error) {
        super(error, ParametroNoValidoException.MENSAJE, AutenticacionException.ESTADO_ERROR_PARAMETRO);
    }
}
