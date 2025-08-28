package co.com.crediya.r2dbc;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.r2dbc.entity.UsuarioEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Integer,
        UsuarioReactiveRepository

        > implements UsuarioRepository {
    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }


    @Override
    public Mono<Usuario> registrar(Usuario usuario) {
        return super.save(usuario);
    }

    @Override
    public Mono<Usuario> buscarPorEmail(String email) {
        return super.repository.buscarUsuarioPorEmail(email);
    }

    @Override
    public Mono<Usuario> buscarPorDocumentIdentidad(String documentIdentidad) {
        return super.repository.buscarUsuarioPorDocumentoIdentidad(documentIdentidad);
    }
}
