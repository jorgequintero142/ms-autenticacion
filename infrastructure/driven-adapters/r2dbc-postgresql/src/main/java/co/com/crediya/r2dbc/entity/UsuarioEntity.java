package co.com.crediya.r2dbc.entity;

import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "Usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {
    @Id
    private Integer id_usuario;
    private String email;
    private String documento_identidad;
    private BigDecimal salario_base;
    private String nombre;
    private String apellido;
    private LocalDate fecha_nacimiento;
    private String direccion;
    private String telefono;

}
