package co.com.crediya.model.usuario.exceptions;


public class ParametroNoValidoException extends AutenticacionException {
    static final String MENSAJE = "Validación fallida";

    public ParametroNoValidoException(String error) {
        super(error, ParametroNoValidoException.MENSAJE, AutenticacionException.ESTADO_ERROR_PARAMETRO);
    }
}
