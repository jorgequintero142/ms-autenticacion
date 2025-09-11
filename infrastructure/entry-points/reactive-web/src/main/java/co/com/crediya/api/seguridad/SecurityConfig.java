package co.com.crediya.api.seguridad;

import co.com.crediya.model.usuario.exceptions.SeguridadException;
import co.com.crediya.usecase.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtAutenticacionFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers("/api/v1/login").permitAll()
                        .pathMatchers("/api/v1/validartoken").authenticated()
                        .pathMatchers("/api/v1/usuarios").hasAnyRole("Administrador", "Asesor")
                        .pathMatchers(
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/api-docs**"
                        ).permitAll()

                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accesoDenegadoPorRol())
                )
                .addFilterAt(jwtAutenticacionFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .build();
    }

    @Bean
    public ServerAccessDeniedHandler accesoDenegadoPorRol() {
        return (exchange, denied) ->
                Mono.error(new SeguridadException(Constantes.ERROR_ROL_NECESARIO));
    }
}