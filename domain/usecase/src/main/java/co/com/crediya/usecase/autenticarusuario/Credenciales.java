package co.com.crediya.usecase.autenticarusuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Credenciales {
    private String email;
    private String contrasenia;
}
