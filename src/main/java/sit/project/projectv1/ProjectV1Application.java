package sit.project.projectv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "sit.project.projectv1.repositories")
//@EntityScan(basePackages = "sit.project.projectv1.repositories")
//@ComponentScan(basePackages = "sit.project.projectv1.repositories.impl")
public class ProjectV1Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectV1Application.class, args);
    }

}
