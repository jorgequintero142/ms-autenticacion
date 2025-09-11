package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Rol;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolRepository {
    Flux<Rol> listarTodos();

    Mono<Rol> buscarPorId(int id);
}
