package co.com.crediya.api;

import co.com.crediya.api.dto.RespuestaApi;
import co.com.crediya.model.seguridad.TokenGenerado;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.UsuarioCreado;
import co.com.crediya.usecase.autenticarusuario.AutenticarUsuarioUseCase;
import co.com.crediya.usecase.autenticarusuario.Credenciales;
import co.com.crediya.usecase.buscarusuario.BuscarUsuarioUseCase;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {
    private final RegistrarUsuarioUseCase registrarUsuarioCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;

    private static final String USUARIO_CREADO = "Usuario creado";
    private static final String USUARIO_ENCONTRADO = "Usuario encontrado";
    private static final String AUTENTICACION_EXITO = "Se realizó la autenticación con exito";
    private static final int CODIGO_ESTADO_OK = 200;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioHandler.class);


    @Operation(
            summary = "Crear un nuevo usuario",
            tags = {"Usuarios"},
            description = "Crea un nuevo usuario en el sistema",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """
                                            {
                                                    "nombre": "Braden",
                                                    "apellido": "Dach",
                                                    "email": "Bette25@hotmail.com",
                                                    "documentoIdentidad": "1757599389",
                                                    "fechaNacimiento": "1986-08-28",
                                                    "direccion": "calle 123 23-23",
                                                    "telefono": "3001231212",
                                                    "salarioBase": 1500000,
                                                    "idRol": 2
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario creado",
                            content = @Content(
                                    schema = @Schema(
                                            example = """
                                                    {
                                                        "estado": 200,
                                                        "mensaje": "Usuario creado",
                                                        "data": {
                                                            "nombre": "Braden",
                                                            "apellido": "Dach",
                                                            "email": "Bette25@hotmail.com",
                                                            "documentoIdentidad": "1757599389",
                                                            "fechaNacimiento": "1986-08-28",
                                                            "direccion": "calle 123 23-23",
                                                            "telefono": "3001231212",
                                                            "salarioBase": 1500000,
                                                            "contrasenia": "c05e5c7",
                                                            "nombreRol": "Asesor"
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validación fallida",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            description = "Respuesta de error",
                                            example = """
                                                    {
                                                      "mensaje": "Validación fallida",
                                                      "error": "Fecha de nacimiento no tiene un formato válido",
                                                      "status": 400
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    public Mono<ServerResponse> registrar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Usuario.class)
                .doOnNext(usuario-> logger.info("UsuarioHandler::registrar request {}",usuario))
                .flatMap(registrarUsuarioCase::registrar)
                .doOnSuccess(usuarioGuardado -> logger.info("UsuarioHandler::registrar response {}", usuarioGuardado))
                .doOnError(error -> logger.error("UsuarioHandler::registrar error", error))
                .flatMap(usuarioGuardado -> {
                    RespuestaApi<UsuarioCreado> respuesta = new RespuestaApi<>(UsuarioHandler.CODIGO_ESTADO_OK, UsuarioHandler.USUARIO_CREADO, usuarioGuardado);
                    logger.info("UsuarioHandler::registrar response {}",usuarioGuardado);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(respuesta);
                });

    }



    @Operation(
            summary = "Obtener usuario por Numero de documento",
            tags = {"Usuarios"},
            parameters = {
                    @Parameter(
                            name = "documentoIdentidad",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "Numero de documento del usuario"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario encontrado.",
                            content = @Content( mediaType = "application/json",
                                    schema = @Schema(description = "Respuesta usuario encontrado",
                                            example = """
                                    {
                                        "estado": 200,
                                        "mensaje": "Usuario encontrado",
                                        "data": {
                                            "nombre": "Braden",
                                            "apellido": "Dach",
                                            "email": "Bette25@hotmail.com",
                                            "documentoIdentidad": "1757599389",
                                            "fechaNacimiento": "1986-08-28",
                                            "direccion": "calle 123 23-23",
                                            "telefono": "3001231212",
                                            "salarioBase": 1500000.00,
                                            "idRol": 2,
                                            "contrasenia": "c05e5c7"
                                        }
                                    }
                                    """))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            description = "Respuesta de error",
                                            example = """
                                                    {
                                                      "mensaje": "No encontrado",
                                                      "error": "No se encontró usuario con el número de documento enviado",
                                                      "status": 404
                                                    }
                                                    """
                                    )
                            ))
            }
    )

    public Mono<ServerResponse> buscarPorDocumento(ServerRequest serverRequest) {
        String documentoIdentidad = serverRequest.pathVariable("documentoIdentidad");

        return buscarUsuarioUseCase.buscarPorDocumentoIdentidad(documentoIdentidad)
                .flatMap(usuario -> {
                    RespuestaApi<Usuario> respuesta = new RespuestaApi<>(UsuarioHandler.CODIGO_ESTADO_OK, UsuarioHandler.USUARIO_ENCONTRADO, usuario);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(respuesta);
                });
    }



    @Operation(
            summary = "Realizar autenticacion en el sistema",
            tags = {"Seguridad"},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """
                                            {
                                                "email": "admin@admin.com",
                                                "contrasenia": "123456"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticación con exito",
                            content = @Content( mediaType = "application/json",
                                    schema = @Schema(description = "Respuesta login correcto",
                                            example = """
                                                    {
                                                        "estado": 200,
                                                        "mensaje": "Se realizó la autenticación con exito",
                                                        "data": {
                                                            "token": "eyJhbGciOiJIUzI1NiJ9"
                                                        }
                                                    }
                                    """))
                    ),
                    @ApiResponse(responseCode = "400", description = "Login no exitoso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            description = "Respuesta con causa del error",
                                            example = """
                                                    {
                                                        "error": "Contraseña incorrecta",
                                                        "mensaje": "Validación fallida",
                                                        "estado": "400"
                                                    }
                                                    """
                                    )
                            ))
            }
    )
    public Mono<ServerResponse> autenticar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Credenciales.class)
               .doOnNext(payload -> logger.info("Se intento hacer login con {}", payload))
                .flatMap(credenciales ->
                        autenticarUsuarioUseCase.autenticarUsuario(
                                        credenciales
                                )
                                .doOnSuccess(tokenGenerado -> logger.info("UsuarioHandler::autenticar response {}", tokenGenerado))
                                .doOnError(error -> logger.error("UsuarioHandler::autenticar error", error))
                                .flatMap(token -> {
                                            RespuestaApi<TokenGenerado> respuesta = new RespuestaApi<>(UsuarioHandler.CODIGO_ESTADO_OK, UsuarioHandler.AUTENTICACION_EXITO, token);
                                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(respuesta);
                                        }
                                )
                );
    }
}
