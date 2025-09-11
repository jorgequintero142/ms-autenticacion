package co.com.crediya.r2dbc;

import co.com.crediya.model.usuario.Rol;
import co.com.crediya.model.usuario.gateways.RolRepository;
import co.com.crediya.r2dbc.entity.RolEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        Integer,
        RolReactiveRepository

        > implements RolRepository {


    public RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class));

    }

    @Override
    public Flux<Rol> listarTodos() {
        return repository.findAll().map(rolEntity ->
                mapper.map(rolEntity, Rol.class));
    }

    @Override
    public Mono<Rol> buscarPorId(int id) {
        return repository.findById(id).map(rolEntity -> mapper.map(rolEntity, Rol.class));
    }
}
