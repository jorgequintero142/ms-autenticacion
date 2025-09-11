package co.com.crediya.model.seguridad.gateways;

import co.com.crediya.model.seguridad.InformacionUsuario;
import co.com.crediya.model.seguridad.TokenGenerado;
import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface AutenticacionService {
    Mono<TokenGenerado> generarToken(Usuario usuario);

    Mono<InformacionUsuario> validarToken(String token);
}
