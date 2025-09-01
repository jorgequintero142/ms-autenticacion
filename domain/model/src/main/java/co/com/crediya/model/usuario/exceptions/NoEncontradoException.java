package co.com.crediya.model.usuario.exceptions;

public class NoEncontradoException extends AutenticacionException {
    static final String ERROR = "No encontrado";

    public NoEncontradoException(String mensaje) {
        super(mensaje, NoEncontradoException.ERROR, NoEncontradoException.HTTP_NOT_FOUND);
    }
}
