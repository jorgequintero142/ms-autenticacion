package co.com.crediya.usecase.buscarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.NoEncontradoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.Constantes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BuscarUsuarioUseCase {
    private final UsuarioRepository usuarioRepositorio;

    public Mono<Usuario> buscarPorDocumentoIdentidad(String documentIdentidad) {
        return usuarioRepositorio.buscarPorDocumentoIdentidad(documentIdentidad).switchIfEmpty(Mono.error(new NoEncontradoException(Constantes.USUARIO_NO_ENCONTRADO_NUMERO_DOCUMENTO)));
    }

    public Mono<Usuario> buscarPorEmail(String email) {
        return usuarioRepositorio.buscarPorEmail(email).switchIfEmpty(Mono.error(new NoEncontradoException(Constantes.USUARIO_NO_ENCONTRADO_EMAIL)));
    }
}