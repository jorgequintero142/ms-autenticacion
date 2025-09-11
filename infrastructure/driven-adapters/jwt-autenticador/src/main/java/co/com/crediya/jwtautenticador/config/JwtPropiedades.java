package co.com.crediya.jwtautenticador.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtPropiedades {
    public String secretKey;
    public long expiracion;
}
