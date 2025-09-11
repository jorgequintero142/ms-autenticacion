package co.com.crediya.model.seguridad;
import lombok.*;

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
