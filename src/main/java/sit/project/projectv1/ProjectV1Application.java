package sit.project.projectv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import sit.project.projectv1.properties.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
@EnableScheduling
public class ProjectV1Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectV1Application.class, args);
    }

}
