package co.com.crediya.usecase.autenticarusuario;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Credenciales {
    private String email;
    private String contrasenia;
}
