package sit.project.projectv1.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private Integer tokenIntervalInMinute;
    private Integer refreshTokenIntervalInHour;
}
