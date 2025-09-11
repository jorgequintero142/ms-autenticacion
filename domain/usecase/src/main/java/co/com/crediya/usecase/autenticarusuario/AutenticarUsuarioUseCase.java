package co.com.crediya.usecase.autenticarusuario;

import co.com.crediya.model.seguridad.InformacionUsuario;
import co.com.crediya.model.seguridad.TokenGenerado;
import co.com.crediya.model.seguridad.gateways.AutenticacionService;
import co.com.crediya.model.usuario.exceptions.ParametroNoValidoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticarUsuarioUseCase {
    private final AutenticacionService autenticacionService;
    private final UsuarioRepository usuarioRepositorio;

    public Mono<TokenGenerado> autenticarUsuario(Credenciales credenciales) {

        return usuarioRepositorio.buscarPorEmail(credenciales.getEmail())
                .switchIfEmpty(reactor.core.publisher.Mono.error(new ParametroNoValidoException(Constantes.USUARIO_NO_ENCONTRADO)))
                .flatMap(usuario -> {
                    if (!credenciales.getContrasenia().equals(usuario.getContrasenia())) {
                        return reactor.core.publisher.Mono.error(new ParametroNoValidoException(Constantes.CONTRASENIA_INCORRECTA));
                    }
                    return autenticacionService.generarToken(usuario);
                });
    }

    public Mono<InformacionUsuario> validarToken(String token) {
        return autenticacionService.validarToken(token);
    }


}
