package co.com.crediya.usecase.buscarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.NoEncontradoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuscarUsuarioTest {

    private BuscarUsuarioUseCase buscarUsuarioUseCase;
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        buscarUsuarioUseCase = new BuscarUsuarioUseCase(usuarioRepository);
    }


    @Test
    void buscarPorDocumentoIdentidad() {
        co.com.crediya.model.usuario.Usuario usuario = new Usuario();
        String documento = "12345";
        usuario.setDocumentoIdentidad(documento);


        when(usuarioRepository.buscarPorDocumentoIdentidad(documento)).thenReturn(Mono.just(usuario));
        StepVerifier.create(buscarUsuarioUseCase.buscarPorDocumentoIdentidad(documento))
                .expectNext(usuario)
                .verifyComplete();

        verify(usuarioRepository).buscarPorDocumentoIdentidad(documento);
    }

    @Test
    void buscarEmail() {
        co.com.crediya.model.usuario.Usuario usuario = new Usuario();
        String email = "mail@mail.com";
        usuario.setEmail(email);


        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Mono.just(usuario));
        StepVerifier.create(buscarUsuarioUseCase.buscarPorEmail(email))
                .expectNext(usuario)
                .verifyComplete();

        verify(usuarioRepository).buscarPorEmail(email);
    }

    @Test
    void buscarEmailNoEncontrado() {


        String documento = "123456";
        when(usuarioRepository.buscarPorDocumentoIdentidad(documento))
                .thenReturn(Mono.empty());


        StepVerifier.create(buscarUsuarioUseCase.buscarPorDocumentoIdentidad(documento))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoEncontradoException &&
                                throwable.getMessage().equals("No encontrado")
                )
                .verify();

        verify(usuarioRepository).buscarPorDocumentoIdentidad(documento);
    }
}
