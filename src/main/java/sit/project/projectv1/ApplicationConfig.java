package sit.project.projectv1;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sit.project.projectv1.utils.ListMapper;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ListMapper listMapper() {
        return ListMapper.getInstance();
    }
}
