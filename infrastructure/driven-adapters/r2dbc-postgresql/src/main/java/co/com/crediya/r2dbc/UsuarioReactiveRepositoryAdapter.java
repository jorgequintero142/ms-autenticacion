package co.com.crediya.r2dbc;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.r2dbc.entity.UsuarioEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Integer,
        UsuarioReactiveRepository

        > implements UsuarioRepository {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioReactiveRepositoryAdapter.class);

    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }


    @Override
    public Mono<Usuario> registrar(Usuario usuario) {
        logger.debug("Se realiza peticion de creación de nuevo usuario con parametros {}", usuario);
        UsuarioEntity entity = mapper.map(usuario, UsuarioEntity.class);
        return repository.save(entity)
                .map(saved -> {
                    return mapper.map(saved, Usuario.class);
                })
                .doOnNext(u -> logger.debug("Se ha registrado un nuevo usuario"));
    }

    @Override
    public Mono<Usuario> buscarPorEmail(String email) {
        logger.debug("Se inicia busqueda de usuario por email con valor {}", email);
        return super.repository.buscarUsuarioPorEmail(email);
    }

    @Override
    public Mono<Usuario> buscarPorDocumentoIdentidad(String documentIdentidad) {
        logger.debug("Se inicia busqueda de usuario por documento con valor {}", documentIdentidad);
        return super.repository.buscarUsuarioPorDocumentoIdentidad(documentIdentidad);
    }
}
