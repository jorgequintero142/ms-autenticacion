package co.com.crediya.jwtautenticador;

import co.com.crediya.jwtautenticador.config.JwtPropiedades;
import co.com.crediya.model.seguridad.ConstantesSeguridad;
import co.com.crediya.model.seguridad.InformacionUsuario;
import co.com.crediya.model.seguridad.TokenGenerado;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.RolRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtAutenticadorService implements JwtAutenticadorServiceInterface {
    private final JwtPropiedades jtwPropiedades;
    private final RolRepository rolRepository;

    public Mono<TokenGenerado> generarToken(Usuario usuario) {
        return rolRepository.buscarPorId(usuario.getIdRol())
                .map(rol ->
                        Jwts.builder()
                                .setSubject(usuario.getEmail())
                                .claim(ConstantesSeguridad.CLAIM_ROL, rol.getNombre())
                                .claim(ConstantesSeguridad.CLAIM_DOCUMENTO, usuario.getDocumentoIdentidad())
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + jtwPropiedades.expiracion))
                                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                                .compact()
                ).map(TokenGenerado::new);
    }

    @Override
    public Mono<InformacionUsuario> validarToken(String token) {
        return Mono.fromCallable(() -> Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
        ).flatMap(claims -> {
            InformacionUsuario informacionUsuario =     InformacionUsuario.builder()
                    .rol(claims.get(ConstantesSeguridad.CLAIM_ROL, String.class))
                    .subject(claims.getSubject())
                    .documento(claims.get(ConstantesSeguridad.CLAIM_DOCUMENTO, String.class))
                    .build();

            return Mono.just(informacionUsuario);
        });
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jtwPropiedades.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
