package co.com.crediya.api;

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
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Crear un nuevo usuario",
            tags = {"Usuarios"},
            description = "Crea un nuevo usuario en el sistema",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = Usuario.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario creado",
                            content = @Content(
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                            implementation = Usuario.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validación fallida"
                    )
            }
    )
    public Mono<ServerResponse> registrar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Usuario.class)
                .flatMap(registrarUsuarioCase::registrar)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }


    @Operation(
            summary = "Obtener usuario por Numero de dcumento",
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
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
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
