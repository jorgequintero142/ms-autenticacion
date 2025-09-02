package co.com.crediya.api;

import co.com.crediya.model.usuario.exceptions.AutenticacionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AutenticacionException.class)
    public Mono<ResponseEntity<Map<String, Object>>> manejarErrores(AutenticacionException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getError());
        error.put("mensaje", ex.getMessage());
        error.put("estado", ex.getCodigoEstado());
        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(ex.getCodigoEstado())).body(error));
    }
}
