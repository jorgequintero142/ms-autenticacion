package co.com.crediya.usecase.autenticarusuario;

import co.com.crediya.model.seguridad.TokenGenerado;
import co.com.crediya.model.seguridad.gateways.AutenticacionService;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.ParametroNoValidoException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AutenticarUsuarioTest {


    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private AutenticacionService autenticacionService;
    private UsuarioRepository usuarioRepositorio;
    private Usuario usuario;
    private TokenGenerado tokenGenerado;
    private Credenciales credenciales;

    @BeforeEach
    void setUp() {
        usuarioRepositorio = Mockito.mock(UsuarioRepository.class);
        autenticacionService = Mockito.mock(AutenticacionService.class);

        autenticarUsuarioUseCase = new AutenticarUsuarioUseCase(autenticacionService, usuarioRepositorio);

        credenciales = new Credenciales("mail@mail.com", "12345");
        usuario = Usuario.builder()
                .email("jorge@mail.com")
                .documentoIdentidad("123456")
                .contrasenia("12345")
                .build();

        tokenGenerado = TokenGenerado.builder().token("12344").build();
        when(usuarioRepositorio.buscarPorEmail(credenciales.getEmail())).thenReturn(Mono.just(usuario));
        when(autenticacionService.generarToken(any(Usuario.class))).thenReturn(Mono.just(tokenGenerado));


    }


    @Test
    void autenticarConExito() {
        StepVerifier.create(autenticarUsuarioUseCase.autenticarUsuario(credenciales))
                .expectNext(tokenGenerado)
                .verifyComplete();

        verify(usuarioRepositorio).buscarPorEmail(credenciales.getEmail());
        verify(autenticacionService).generarToken(usuario);
    }

    @Test
    void autenticarUsuarioNoEncontrado() {

        Credenciales credencialesNoUsuario = new Credenciales("noencontrado@mail.com", "123456");
        when(usuarioRepositorio.buscarPorEmail(credencialesNoUsuario.getEmail()))
                .thenReturn(Mono.empty());

        StepVerifier.create(autenticarUsuarioUseCase.autenticarUsuario(credencialesNoUsuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.USUARIO_NO_ENCONTRADO);
                })
                .verify();

        verify(usuarioRepositorio).buscarPorEmail(credencialesNoUsuario.getEmail());

    }

    @Test
    void autenticarFallaContrasenia() {
        Credenciales credenciales = new Credenciales("correo@mail.com", "1234566");
        when(usuarioRepositorio.buscarPorEmail(credenciales.getEmail()))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(autenticarUsuarioUseCase.autenticarUsuario(credenciales))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.CONTRASENIA_INCORRECTA);
                })
                .verify();

        verify(usuarioRepositorio).buscarPorEmail(credenciales.getEmail());

    }
}