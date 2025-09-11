package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {
    @Id
    @Column("id_usuario")
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    @Column("documento_identidad")
    private String documentoIdentidad;
    @Column("salario_base")
    private BigDecimal salarioBase;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    @Column("id_rol")
    private int idRol;
    private String contrasenia;

}
