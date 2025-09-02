package co.com.crediya.api;

import co.com.crediya.api.dto.RespuestaApi;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.usecase.buscarusuario.BuscarUsuarioUseCase;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {
    private final RegistrarUsuarioUseCase registrarUsuarioCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private static final String USUARIO_CREADO = "Usuario creado";
    private static final int CODIGO_ESTADO = 200;

    @Operation(
            summary = "Crear un nuevo usuario",
            tags = {"Usuarios"},
            description = "Crea un nuevo usuario en el sistema",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    example = """
                                            {
                                              "idUsuario": 9,
                                              "nombre": "jorge",
                                              "apellido": "quintero",
                                              "email": "jorgequintero142@gmail.com",
                                              "documentoIdentidad": "101559999",
                                              "fechaNacimiento": "1986-08-28",
                                              "direccion": "calle 123 23-23",
                                              "telefono": "3001231212",
                                              "salarioBase": 1500000
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
                                                      "idUsuario": 9,
                                                      "nombre": "jorge",
                                                      "apellido": "quintero",
                                                      "email": "jorgequintero142@gmail.com",
                                                      "documentoIdentidad": "101559999",
                                                      "fechaNacimiento": "1986-08-28",
                                                      "direccion": "calle 123 23-23",
                                                      "telefono": "3001231212",
                                                      "salarioBase": 1500000
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
                                                      "error": "Validación fallida",
                                                      "message": "Fecha de nacimiento no tiene un formato válido",
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
                .flatMap(registrarUsuarioCase::registrar)
                .flatMap(usuarioGuardado -> {
                    RespuestaApi<Usuario> respuesta = new RespuestaApi<>(UsuarioHandler.CODIGO_ESTADO, UsuarioHandler.USUARIO_CREADO, usuarioGuardado);
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
                            description = "Usuario encontrado",
                            content = @Content(schema = @Schema(implementation = Usuario.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            description = "Respuesta de error",
                                            example = "{ \"error\": \"No encontrado\", \"message\": \"Usuario no encontrado con documento: 123456\", \"status\": 404 }"
                                    )
                            ))
            }
    )
    public Mono<ServerResponse> buscarPorDocumento(ServerRequest serverRequest) {
        String documentoIdentidad = serverRequest.pathVariable("documentoIdentidad");

        return buscarUsuarioUseCase.buscarPorDocumentoIdentidad(documentoIdentidad)
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
