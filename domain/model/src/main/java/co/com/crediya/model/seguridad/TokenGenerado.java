package co.com.crediya.model.seguridad;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenGenerado {
    private String token;
}
