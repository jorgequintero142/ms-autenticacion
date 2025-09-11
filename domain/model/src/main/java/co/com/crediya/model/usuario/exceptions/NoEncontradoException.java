package co.com.crediya.model.usuario.exceptions;

import co.com.crediya.model.exceptions.AutenticacionException;

public class NoEncontradoException extends AutenticacionException {
    static final String MENSAJE = "No encontrado";

    public NoEncontradoException(String error) {
        super(error, NoEncontradoException.MENSAJE, AutenticacionException.ESTADO_NO_ENCONTRADO);
    }
}
