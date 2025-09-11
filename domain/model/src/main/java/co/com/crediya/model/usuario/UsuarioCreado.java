package co.com.crediya.model.usuario;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class UsuarioCreado {
    private String nombre;
    private String apellido;
    private String email;
    private String documentoIdentidad;
    private java.time.LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private java.math.BigDecimal salarioBase;
    private String contrasenia;
    private String nombreRol;
}
