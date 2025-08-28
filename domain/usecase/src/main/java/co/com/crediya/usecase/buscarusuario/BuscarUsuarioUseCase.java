package co.com.crediya.usecase.buscarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.NoEncontradoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BuscarUsuarioUseCase {
    private final UsuarioRepository usuarioRepositorio;

    public Mono<Usuario> buscarPorDocumentoIdentidad(String documentIdentidad) {
        return usuarioRepositorio.buscarPorDocumentIdentidad(documentIdentidad).switchIfEmpty(Mono.error(new NoEncontradoException("Usuario no encontrado con documento: " + documentIdentidad)));
    }
}