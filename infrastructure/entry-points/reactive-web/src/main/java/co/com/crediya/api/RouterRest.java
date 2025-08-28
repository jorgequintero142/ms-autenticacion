package co.com.crediya.api;

import co.com.crediya.model.usuario.Usuario;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({@RouterOperation(
            path = "/api/v1/soli",
            beanClass = UsuarioHandler.class,
            beanMethod = "registrar",
            operation = @Operation(
                    summary = "Crear un nuevo usuario",
                    description = "Crea un nuevo usuario en el sistema",
                    tags = {"Usuarios"},
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
    ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{documentoIdentidad}",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "buscarPorDocumento",
                    operation = @Operation(
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
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler handler) {
        return route(POST("/api/v1/usuarios"), handler::registrar)
                .andRoute(GET("/api/v1/usuarios/{documentoIdentidad}"), handler::buscarPorDocumento)
                ;
    }
}
