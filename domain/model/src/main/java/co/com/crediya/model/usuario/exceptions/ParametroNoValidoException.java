package co.com.crediya.model.usuario.exceptions;


public class ParametroNoValidoException extends AutenticacionException {
    static final String ERROR = "Validación fallida";

    public ParametroNoValidoException(String mensaje) {

        super(mensaje, ParametroNoValidoException.ERROR, ParametroNoValidoException.HTTP_BAD_REQUEST);
    }
}
