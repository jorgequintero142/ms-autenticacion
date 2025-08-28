package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.ParametroNoValidoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository usuarioRepositorio;
    private final BigDecimal SALARIO_MINIMO_PERMITIDO = BigDecimal.valueOf(0);
    private final BigDecimal SALARIO_MAXIMO_PERMITIDO = BigDecimal.valueOf(15000000);
    private final int MINIMA_LONGITUD_DOCUMENTO = 5;
    private final int EDAD_MINIMA_PERMITIDA = 18;


    public Mono<Usuario> registrar(Usuario usuario) {
        return
                validarEmail(usuario)
                        .flatMap(this::validarUsuario)
                        .flatMap(usuarioRepositorio::registrar);
    }


    private Mono<Usuario> validarUsuario(Usuario usuario) {
        if (!esValidoNombre(usuario.getApellido())) {
            return Mono.error(new ParametroNoValidoException("El apellido  no tiene un formato válido"));
        }

        if (!esValidoNombre(usuario.getNombre())) {
            return Mono.error(new ParametroNoValidoException("El nombre  no tiene un formato válido"));
        }
        if (usuario.getSalarioBase() == null ||
                usuario.getSalarioBase().compareTo(SALARIO_MINIMO_PERMITIDO) < 0 ||
                usuario.getSalarioBase().compareTo(SALARIO_MAXIMO_PERMITIDO) > 0) {
            return Mono.error(new ParametroNoValidoException(
                    "El salario debe estar entre " + SALARIO_MINIMO_PERMITIDO + " y " + SALARIO_MAXIMO_PERMITIDO
            ));
        }
        if (esVacioNulo(usuario.getDocumentoIdentidad()) || usuario.getDocumentoIdentidad().length() < this.MINIMA_LONGITUD_DOCUMENTO) {
            return Mono.error(new ParametroNoValidoException(
                    "El documento de identidad no tiene el formato esperado"
            ));
        }
        if (!esValidaFecha(usuario.getFechaNacimiento())) {
            return Mono.error(new ParametroNoValidoException(
                    "Fecha de nacimiento no tiene un formato válido"
            ));
        }


        if (!esValidoElTelefono(usuario.getTelefono())) {
            return Mono.error(new ParametroNoValidoException(
                    "El telefono no tiene un formato válido"
            ));
        }
        return Mono.just(usuario);
    }


    Mono<Usuario> validarEmail(Usuario usuario) {
        if (esVacioNulo(usuario.getEmail()) || !isOkEmail(usuario.getEmail())) {
            return Mono.error(new ParametroNoValidoException("El email no tiene un formato válido"));
        }
        return usuarioRepositorio.buscarPorEmail(usuario.getEmail())
                .hasElement()
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ParametroNoValidoException(
                                "El email ya está registrado"
                        ));
                    }
                    return Mono.just(usuario);
                });
    }

    private boolean isOkEmail(String valor) {
        return valor.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean esVacioNulo(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private boolean esValidaFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fecha, hoy).getYears();
        return edad >= this.EDAD_MINIMA_PERMITIDA;
    }

    private boolean esValidoNombre(String valor) {
        return !esVacioNulo(valor) && valor.matches("^[a-zA-Z]+$");
    }

    private boolean esValidoElTelefono(String valor) {
        return !esVacioNulo(valor) && valor.matches("^3\\d{9}$");
    }
}
