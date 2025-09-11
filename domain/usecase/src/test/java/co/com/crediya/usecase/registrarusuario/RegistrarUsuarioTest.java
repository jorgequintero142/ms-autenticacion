package co.com.crediya.usecase.registrarusuario;


import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.ParametroNoValidoException;
import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrarUsuarioTest {
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private Rol rolCliente, rolAsesor;

    private Usuario crearUsuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Jorge");
        usuario.setApellido("Quintero");
        usuario.setEmail("jorge142@gmail.com");
        usuario.setDocumentoIdentidad("101231211");
        usuario.setFechaNacimiento(LocalDate.now().minusYears(39));
        usuario.setTelefono("3001231212");
        usuario.setDireccion("Calle 123");
        usuario.setIdRol(3);
        usuario.setContrasenia("pass123");
        usuario.setSalarioBase(BigDecimal.valueOf(2000));
        return usuario;
    }


    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        rolRepository = Mockito.mock(RolRepository.class);

        registrarUsuarioUseCase = new RegistrarUsuarioUseCase(usuarioRepository, rolRepository);

        rolCliente = new Rol();
        rolCliente.setIdRol(3);
        rolCliente.setNombre("Cliente");

        rolAsesor = new Rol();
        rolAsesor.setIdRol(2);
        rolAsesor.setNombre("Asesor");

        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Mono.empty());
        when(usuarioRepository.buscarPorDocumentoIdentidad(anyString())).thenReturn(Mono.empty());
        when(rolRepository.listarTodos()).thenReturn(Flux.just(rolCliente, rolAsesor));
    }


    @Test
    void registarConExito() {
        Usuario usuario = crearUsuarioValido();

        when(usuarioRepository.buscarPorEmail(usuario.getEmail())).thenReturn(Mono.empty());
        when(usuarioRepository.buscarPorDocumentoIdentidad(usuario.getDocumentoIdentidad())).thenReturn(Mono.empty());
        when(usuarioRepository.registrar(any(Usuario.class))).thenReturn(Mono.just(usuario));

        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectNextMatches(u ->
                        u.getNombre().equals(usuario.getNombre()) &&
                                u.getApellido().equals(usuario.getApellido()) &&
                                u.getNombreRol().equals("Cliente")
                )
                .verifyComplete();

        verify(usuarioRepository).registrar(usuario);
    }

    @Test
    void emailConFormatoIncorrecto() {
        Usuario usuario = this.crearUsuarioValido();
        usuario.setEmail("email-con.error");
        when(usuarioRepository.buscarPorDocumentoIdentidad(usuario.getDocumentoIdentidad())).thenReturn(Mono.empty());

        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_EMAIL);
                })
                .verify();
    }


    @Test
    void emailYaExisteIncorrecto() {
        Usuario usuario = this.crearUsuarioValido();
        when(usuarioRepository.buscarPorDocumentoIdentidad(usuario.getDocumentoIdentidad())).thenReturn(Mono.empty());
        when(usuarioRepository.buscarPorEmail(usuario.getEmail())).thenReturn(Mono.just(usuario));

        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_EMAIL_REGISTRADO);
                })
                .verify();
    }


    @Test
    void documentoYaRegistrado() {
        Usuario usuario = this.crearUsuarioValido();
        when(usuarioRepository.buscarPorDocumentoIdentidad(usuario.getDocumentoIdentidad())).thenReturn(Mono.just(usuario));
        when(usuarioRepository.buscarPorEmail(usuario.getEmail())).thenReturn(Mono.empty());
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_DOCUMENTO_REGISTRADO);
                })
                .verify();
    }


    @Test
    void nombreConMalFormato() {
        Usuario usuario = this.crearUsuarioValido();
        usuario.setNombre("Jorge123");
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_NOMBRE);
                })
                .verify();
    }

    @Test
    void apellidoConMalFormato() {
        Usuario usuario = this.crearUsuarioValido();
        usuario.setApellido("Quint3ro");
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_APELLIDO);
                })
                .verify();
    }

    @Test
    void salarioConMalFormato() {
        Usuario usuario = this.crearUsuarioValido();
        usuario.setSalarioBase(BigDecimal.valueOf(-1));
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_RANGO_SALARIO);
                })
                .verify();
    }


    @Test
    void documentoConMalFormato() {
        Usuario usuario = this.crearUsuarioValido();
        usuario.setDocumentoIdentidad("21");
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_DOCUMENTO_IDENTIDAD);
                })
                .verify();
    }

    @Test
    void fechaConMalFormato() {
        Usuario usuario = this.crearUsuarioValido();

        usuario.setFechaNacimiento(LocalDate.now().minusYears(14));
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_FECHA_NACIMIENTO);
                })
                .verify();
    }

    @Test
    void telefonoConMalFormato() {
        Usuario usuario = this.crearUsuarioValido();

        usuario.setTelefono("12344");
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(ParametroNoValidoException.class);
                    ParametroNoValidoException ex = (ParametroNoValidoException) error;
                    assertThat(ex.getError()).isEqualTo(Constantes.ERROR_TELEFONO);
                })
                .verify();
    }
}