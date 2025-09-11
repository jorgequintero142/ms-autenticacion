package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.UsuarioCreado;
import co.com.crediya.model.usuario.exceptions.ParametroNoValidoException;
import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository usuarioRepositorio;
    private final RolRepository rolRepository;

    private static final Flux<Rol> roles = Flux.empty();


    public Mono<UsuarioCreado> registrar(Usuario usuario) {
        return
                validarUsuario(usuario)
                        .flatMap(usuarioRepositorio::registrar)
                        .flatMap(usuarioRegistrado ->

                                roles
                                        .switchIfEmpty(rolRepository.listarTodos().cache())
                                        .filter(rol -> rol.getIdRol() == usuarioRegistrado.getIdRol())
                                        .next().flatMap(rol ->
                                                homologarUsuario(usuarioRegistrado, rol.getNombre())
                                        )
                        );
    }

    private Mono<Usuario> validarUsuario(Usuario usuario) {
        return esValidoNombre(usuario.getNombre(), Constantes.ERROR_NOMBRE)
                .then(esValidoNombre(usuario.getApellido(), Constantes.ERROR_APELLIDO))
                .then(validarFecha(usuario.getFechaNacimiento()))
                .then(validarFormatoTelefono(usuario.getTelefono()))
                .then(validarRol(usuario))
                .then(validardocumentoUnico(usuario))
                .then(validarEmail(usuario))
                .then(validarSalario(usuario.getSalarioBase()))
                .then(validarDocumento(usuario.getDocumentoIdentidad()))
                .then(validarContrasenia(usuario));
    }


    private Mono<Void> validarDocumento(String documentoIdentidad) {
        return Mono.defer(() -> {
            if (documentoIdentidad == null ||
                    documentoIdentidad.length() < Constantes.MINIMA_LONGITUD_DOCUMENTO) {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_DOCUMENTO_IDENTIDAD));
            }
            return Mono.empty();
        });
    }

    private Mono<Void> validarSalario(BigDecimal salarioBase) {
        return Mono.defer(() -> {
            if (salarioBase == null ||
                    salarioBase.compareTo(Constantes.SALARIO_MINIMO_PERMITIDO) < 0 ||
                    salarioBase.compareTo(Constantes.SALARIO_MAXIMO_PERMITIDO) > 0) {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_RANGO_SALARIO));
            }
            return Mono.empty();
        });
    }

    private Mono<Usuario> validarContrasenia(Usuario usuario) {

        return esVacioNulo(usuario.getContrasenia())
                .map(esVacio -> {
                    if (esVacio) {
                        usuario.setContrasenia(UUID.randomUUID().toString().substring(0, 7));
                    }
                    return usuario;
                });
    }

    private Mono<Void> validarRol(Usuario usuario) {
        return roles.switchIfEmpty(rolRepository.listarTodos().cache())
                .filter(rol -> rol.getIdRol() == (usuario.getIdRol()))
                .switchIfEmpty(Mono.error(new ParametroNoValidoException(Constantes.ERROR_ROL)))
                .then();
    }


    private Mono<Usuario> validarEmail(Usuario usuario) {
        return esVacioNulo(usuario.getEmail())
                .flatMap(esVacio -> {
                    if (esVacio) {
                        return Mono.error(new ParametroNoValidoException(Constantes.ERROR_EMAIL));
                    }
                    return validarFormatoEmail(usuario.getEmail())
                            .flatMap(formatoValido -> {
                                if (!formatoValido) {
                                    return Mono.error(new ParametroNoValidoException(Constantes.ERROR_EMAIL));
                                }
                                return usuarioRepositorio.buscarPorEmail(usuario.getEmail())
                                        .flatMap(u -> Mono.error(new ParametroNoValidoException(Constantes.ERROR_EMAIL_REGISTRADO)))
                                        .switchIfEmpty(Mono.just(usuario)).cast(Usuario.class);
                            });
                });
    }


    private Mono<Usuario> validardocumentoUnico(Usuario usuario) {
        return usuarioRepositorio.buscarPorDocumentoIdentidad(usuario.getDocumentoIdentidad())
                .flatMap(u -> Mono.error(new ParametroNoValidoException(Constantes.ERROR_DOCUMENTO_REGISTRADO)))
                .switchIfEmpty(Mono.just(usuario)).cast(Usuario.class);
    }

    private Mono<Boolean> validarFormatoEmail(String valor) {
        return Mono.just(valor.matches(Constantes.REGEX_EMAIL));
    }

    private Mono<Boolean> esVacioNulo(String valor) {
        return Mono.just(valor == null || valor.trim().isEmpty());
    }

    private Mono<Void> validarFecha(LocalDate fecha) {
        return Mono.defer(() -> {
            LocalDate hoy = LocalDate.now();
            int edad = Period.between(fecha, hoy).getYears();
            if (edad < Constantes.EDAD_MINIMA_PERMITIDA) {
                return Mono.error(new ParametroNoValidoException(Constantes.ERROR_FECHA_NACIMIENTO));
            }
            return Mono.empty();
        });
    }

    private Mono<Void> esValidoNombre(String valor, String error) {
        return esVacioNulo(valor)
                .flatMap(esVacio -> {
                    if (!esVacio && valor.matches(Constantes.REGEX_ALPHA)) {
                        return Mono.empty();
                    }
                    return Mono.error(new ParametroNoValidoException(error));
                });
    }

    private Mono<Void> validarFormatoTelefono(String valor) {
        return esVacioNulo(valor)
                .flatMap(esVacio -> {
                    if (!esVacio && valor.matches(Constantes.REGEX_TELEFONO)) {
                        return Mono.empty();
                    }
                    return Mono.error(new ParametroNoValidoException(Constantes.ERROR_TELEFONO));
                });
    }

    private Mono<UsuarioCreado> homologarUsuario(Usuario usuarioRegistrado, String nombreRol) {
        return Mono.defer(() -> {
            UsuarioCreado usuarioCreado = UsuarioCreado.builder()
                    .nombre(usuarioRegistrado.getNombre())
                    .apellido(usuarioRegistrado.getApellido())
                    .email(usuarioRegistrado.getEmail())
                    .documentoIdentidad(usuarioRegistrado.getDocumentoIdentidad())
                    .fechaNacimiento(usuarioRegistrado.getFechaNacimiento())
                    .direccion(usuarioRegistrado.getDireccion())
                    .telefono(usuarioRegistrado.getTelefono())
                    .salarioBase(usuarioRegistrado.getSalarioBase())
                    .contrasenia(usuarioRegistrado.getContrasenia())
                    .nombreRol(nombreRol)
                    .build();
            return Mono.just(usuarioCreado);
        });
    }
}
