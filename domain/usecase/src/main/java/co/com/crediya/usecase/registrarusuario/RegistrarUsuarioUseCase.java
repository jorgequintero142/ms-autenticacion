package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.ParametroNoValidoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository usuarioRepositorio;


    public Mono<Usuario> registrar(Usuario usuario) {
        return
                validarUsuario(usuario)
                        .flatMap(usuarioRepositorio::registrar);
    }

    private Mono<Usuario> validarUsuario(Usuario usuario) {
        return validarDatosBasicos(usuario)
                .then(validardocumentoUnico(usuario))
                .then(validarEmail(usuario));
    }

    private Mono<Void> validarDatosBasicos(Usuario usuario) {
        if (!esValidoNombre(usuario.getApellido())) {
            return Mono.error(new ParametroNoValidoException(Constantes.ERROR_APELLIDO));
        }
        if (!esValidoNombre(usuario.getNombre())) {
            return Mono.error(new ParametroNoValidoException(Constantes.ERROR_NOMBRE));
        }
        if (usuario.getSalarioBase() == null ||
                usuario.getSalarioBase().compareTo(Constantes.SALARIO_MINIMO_PERMITIDO) < 0 ||
                usuario.getSalarioBase().compareTo(Constantes.SALARIO_MAXIMO_PERMITIDO) > 0) {
            return Mono.error(new ParametroNoValidoException(
                    Constantes.ERROR_RANGO_SALARIO
            ));
        }
        if (esVacioNulo(usuario.getDocumentoIdentidad()) || usuario.getDocumentoIdentidad().length() < Constantes.MINIMA_LONGITUD_DOCUMENTO) {
            return Mono.error(new ParametroNoValidoException(
                    Constantes.ERROR_DOCUMENTO_IDENTIDAD
            ));
        }
        if (!validarFecha(usuario.getFechaNacimiento())) {
            return Mono.error(new ParametroNoValidoException(
                    Constantes.ERROR_FECHA_NACIMIENTO
            ));
        }
        if (!validarFormatoTelefono(usuario.getTelefono())) {
            return Mono.error(new ParametroNoValidoException(
                    Constantes.ERROR_TELEFONO
            ));
        }
        return Mono.empty();
    }


    private Mono<Usuario> validarEmail(Usuario usuario) {
        if (esVacioNulo(usuario.getEmail()) || !validarFormatoEmail(usuario.getEmail())) {
            return Mono.error(new ParametroNoValidoException(Constantes.ERROR_EMAIL));
        }
        return usuarioRepositorio.buscarPorEmail(usuario.getEmail())
                .flatMap(u -> Mono.error(new ParametroNoValidoException(Constantes.ERROR_EMAIL_REGISTRADO)))
                .switchIfEmpty(Mono.just(usuario)).cast(Usuario.class);
    }

    private Mono<Usuario> validardocumentoUnico(Usuario usuario) {
        return usuarioRepositorio.buscarPorDocumentoIdentidad(usuario.getDocumentoIdentidad())
                .flatMap(u -> Mono.error(new ParametroNoValidoException(Constantes.ERROR_DOCUMENTO_REGISTRADO)))
                .switchIfEmpty(Mono.just(usuario)).cast(Usuario.class);
    }

    private boolean validarFormatoEmail(String valor) {
        return valor.matches(Constantes.REGEX_EMAIL);
    }

    private boolean esVacioNulo(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private boolean validarFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fecha, hoy).getYears();
        return edad >= Constantes.EDAD_MINIMA_PERMITIDA;
    }

    private boolean esValidoNombre(String valor) {
        return !esVacioNulo(valor) && valor.matches(Constantes.REGEX_ALPHA);
    }

    private boolean validarFormatoTelefono(String valor) {
        return !esVacioNulo(valor) && valor.matches(Constantes.REGEX_TELEFONO);
    }
}
