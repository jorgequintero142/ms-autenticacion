package co.com.crediya.api;

import co.com.crediya.api.dto.RespuestaApi;
import co.com.crediya.model.seguridad.InformacionUsuario;
import co.com.crediya.model.usuario.exceptions.SeguridadException;
import co.com.crediya.usecase.Constantes;
import co.com.crediya.usecase.autenticarusuario.AutenticarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AutenticacionHandler {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private static final String TOKEN_VALIDO = "Token válido";
    private static final int CODIGO_ESTADO_OK = 200;
    @Operation(
            summary = "Valida el token enviado",
            tags = {"Seguridad"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token válido.",
                            content = @Content( mediaType = "application/json",
                                    schema = @Schema(description = "Respuesta token enviado es válido",
                                            example = """
                                                    {
                                                      "estado": 200,
                                                      "mensaje": "Token válido",
                                                      "data": {
                                                        "rol": "Administrador",
                                                        "documento": "000000",
                                                        "subject": "admin@admin.com"
                                                      }
                                                    }
                                    """))
                    ),
                    @ApiResponse(responseCode = "401", description = "Token no es válido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            description = "Respuesta de error",
                                            example = """
                                                    {
                                                      "error": "Token con error",
                                                      "mensaje": "Error de autorización/autenticacion",
                                                      "estado": "401"
                                                    }
                                                    """
                                    )
                            ))
            }
    )
    public Mono<ServerResponse> validar(ServerRequest request) {
        return leerBearerToken(request)
                .switchIfEmpty(Mono.error(new SeguridadException(Constantes.ERROR_NO_TOKEN)))
                .flatMap(token ->
                        autenticarUsuarioUseCase.validarToken(token)
                                .onErrorMap(throwable -> new SeguridadException(Constantes.ERROR_TOKEN_INVALIDO))
                                .flatMap(claims -> {
                                            RespuestaApi<InformacionUsuario> respuesta = new RespuestaApi<>(AutenticacionHandler.CODIGO_ESTADO_OK, AutenticacionHandler.TOKEN_VALIDO, claims);
                                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(respuesta);
                                        }

                                )
                );
    }


    private Mono<String> leerBearerToken(ServerRequest request) {
        return Mono.justOrEmpty(request.headers().firstHeader("Authorization"))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring("Bearer ".length()));
    }
}