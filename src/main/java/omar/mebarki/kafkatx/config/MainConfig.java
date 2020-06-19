package omar.mebarki.kafkatx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration()
@Import({KafkaConfig.class, JPAConfig.class})
public class MainConfig {
}
