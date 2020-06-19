package omar.mebarki.kafkatx.config;

import omar.mebarki.kafkatx.domain.Message;
import omar.mebarki.kafkatx.repository.MessageRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackageClasses = {Message.class, MessageRepository.class})
@EnableTransactionManagement
@Configuration()
public class JPAConfig {
}
