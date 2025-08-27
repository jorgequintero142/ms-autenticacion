package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository usuarioRepositorio;
    private final BigDecimal SALARIO_MINIMO_PERMITIDO = BigDecimal.valueOf(0);
    private final BigDecimal SALARIO_MAXIMO_PERMITIDO = BigDecimal.valueOf(15000000);

    public Mono<Usuario> save(Usuario usuario) {
        return
                validarEmail(usuario).flatMap(this::validarUsuario)
                        //validarUsuario(usuario)
                        .flatMap(usuarioRepositorio::save);
    }


    private Mono<Usuario> validarUsuario(Usuario usuario) {
        if (isNullOrEmpty(usuario.getEmail())) {
            return Mono.error(new IllegalArgumentException("El email no tiene un formato válido"));
        }

        if (!isValidName(usuario.getApellido())) {
            return Mono.error(new IllegalArgumentException("El apellido  no tiene un formato válido"));
        }
        if (!isValidName(usuario.getNombre())) {
            return Mono.error(new IllegalArgumentException("El nombre  no tiene un formato válido"));
        }
        if (usuario.getSalario_base() == null ||
                usuario.getSalario_base().compareTo(SALARIO_MINIMO_PERMITIDO) < 0 ||
                usuario.getSalario_base().compareTo(SALARIO_MAXIMO_PERMITIDO) > 0) {
            return Mono.error(new IllegalArgumentException(
                    "El salario debe estar entre " + SALARIO_MINIMO_PERMITIDO + " y " + SALARIO_MAXIMO_PERMITIDO
            ));
        }

        if (!isValidPhone(usuario.getTelefono())) {
            return Mono.error(new IllegalArgumentException(
                    "El telefono no tiene un formato válido"
            ));
        }
        return Mono.just(usuario);
    }


    Mono<Usuario> validarEmail(Usuario usuario) {
        if (isNullOrEmpty(usuario.getEmail())) {
            return Mono.error(new IllegalArgumentException("El email no tiene un formato válido"));
        }
        return usuarioRepositorio.findByEmail(usuario.getEmail())
                .hasElement()
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new IllegalArgumentException(
                                "El email ya está registrado"
                        ));
                    }
                    return Mono.just(usuario);
                });
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidName(String valor) {
        return !isNullOrEmpty(valor) && valor.matches("^[a-zA-Z]+$");
    }

    private boolean isValidPhone(String valor) {
        return !isNullOrEmpty(valor) && valor.matches("^3\\d{9}$");
    }
}
