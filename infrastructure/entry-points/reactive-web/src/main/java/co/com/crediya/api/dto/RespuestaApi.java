package co.com.crediya.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespuestaApi<T> {
    private int estado;
    private String mensaje;
    private T data;
}
