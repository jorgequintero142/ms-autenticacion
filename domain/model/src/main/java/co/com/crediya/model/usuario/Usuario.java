package co.com.crediya.model.usuario;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {
    private Integer id_usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String documento_identidad;
    private LocalDate fecha_nacimiento;
    private String direccion;
    private String telefono;
    private BigDecimal salario_base;

}
