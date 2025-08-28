package co.com.crediya.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity(name = "Usuario")
public class UsuarioEntity {
    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    @Column(name = "documento_identidad")
    private String documentoIdentidad;
    @Column(name = "salario_base")
    private BigDecimal salarioBase;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

}
