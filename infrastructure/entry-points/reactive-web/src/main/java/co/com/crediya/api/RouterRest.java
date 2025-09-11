package co.com.crediya.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
            path = "/api/v1/usuarios",
            method = RequestMethod.POST,
            beanClass = UsuarioHandler.class,
            beanMethod = "registrar"
    ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{documentoIdentidad}",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "buscarPorDocumento"
            ),
            @RouterOperation(
                    path = "/api/v1/token",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = AutenticacionHandler.class,
                    beanMethod = "validar"
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "autenticar"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UsuarioHandler usuarioHandler,
                                                         AutenticacionHandler autenticacionHandler
    ) {

        return RouterFunctions.route()
                .POST("/api/v1/login", usuarioHandler::autenticar)
                .POST("/api/v1/usuarios", usuarioHandler::registrar)
                .GET("/api/v1/usuarios/{documentoIdentidad}", usuarioHandler::buscarPorDocumento)
                .GET("/api/v1/token", autenticacionHandler::validar)
                .build();

    }

}
