package omar.mebarki.kafkatx;

import omar.mebarki.kafkatx.config.MainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MainConfig.class)
public class KafkatxApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkatxApplication.class, args);
    }

}
