package co.com.crediya.model.seguridad;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InformacionUsuario {
    private String rol;
    private String documento;
    private String subject;
}
