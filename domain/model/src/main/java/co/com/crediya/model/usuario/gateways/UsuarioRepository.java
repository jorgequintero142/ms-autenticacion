package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> registrar(Usuario usuario);

    Mono<Usuario> buscarPorEmail(String email);

    Mono<Usuario> buscarPorDocumentoIdentidad(String documentoIdentidad);
}
