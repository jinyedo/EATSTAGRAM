package daelim.project.eatstagram;

import daelim.project.eatstagram.storage.StorageProperties;
import daelim.project.eatstagram.storage.StorageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class EatstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatstagramApplication.class, args);
    }

//    @Bean
//    CommandLineRunner init(StorageRepository storageRepository) {
//        return (args) -> {
//            storageRepository.deleteAll();
//            storageRepository.init();
//        };
//    }
}
