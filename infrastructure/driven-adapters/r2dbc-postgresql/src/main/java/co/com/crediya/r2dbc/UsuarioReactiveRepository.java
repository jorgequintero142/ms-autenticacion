package co.com.crediya.r2dbc;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.r2dbc.entity.UsuarioEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Integer>, ReactiveQueryByExampleExecutor<UsuarioEntity> {
    @Query("SELECT * FROM usuario WHERE email = :email")
    Mono<Usuario> buscarUsuarioPorEmail(@Param("email") String email);

    @Query("SELECT * FROM usuario WHERE documento_identidad = :documentoIdentidad")
    Mono<Usuario> buscarUsuarioPorDocumentoIdentidad(@Param("documentoIdentidad") String documentoIdentidad);
}
