package co.com.crediya.api.seguridad;


import co.com.crediya.model.usuario.exceptions.SeguridadException;
import co.com.crediya.usecase.Constantes;
import co.com.crediya.usecase.autenticarusuario.AutenticarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtFilter implements WebFilter {
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
         if (path.equals("/api/v1/login") || path.equals("/api/v1/token")|| path.startsWith("/webjars/swagger-ui/")
                || path.startsWith("/api-docs")) {
            return chain.filter(exchange);
        }
        String token = extractToken(exchange.getRequest());
        if (token == null) {
            return Mono.error(new SeguridadException(Constantes.ERROR_NO_TOKEN));
        }

        return autenticarUsuarioUseCase.validarToken(token)
                .onErrorResume(e -> Mono.error(new SeguridadException(Constantes.ERROR_TOKEN_INVALIDO)))
                .flatMap(claims -> {

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + claims.getRol()))
                    );
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                })
                ;
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7) : null;
    }
}