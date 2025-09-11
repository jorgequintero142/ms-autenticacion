package co.com.crediya.jwtautenticador;

import co.com.crediya.model.seguridad.InformacionUsuario;
import co.com.crediya.model.seguridad.TokenGenerado;
import co.com.crediya.model.usuario.Usuario;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class JwtAutenticadorService implements JwtAutenticadorServiceInterface {
    @Override
    public Mono<TokenGenerado> generarToken(Usuario usuario) {
        return null;
    }

    @Override
    public Mono<InformacionUsuario> validarToken(String token) {
        return null;
    }
}
