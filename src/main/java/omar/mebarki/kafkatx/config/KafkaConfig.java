package omar.mebarki.kafkatx.config;

import omar.mebarki.kafkatx.kafka.listener.MessageListener;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultAfterRollbackProcessor;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.backoff.FixedBackOff;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableKafka
@ComponentScan(basePackageClasses = {MessageListener.class})
public class KafkaConfig {
    @Bean(name = "chainedTm")
    public ChainedKafkaTransactionManager<Object, Object> chainedTm(
            KafkaTransactionManager<String, String> kafkaTransactionManager,
            JpaTransactionManager jpaTransactionManager) {
        ChainedKafkaTransactionManager<Object, Object> transactionManager = new ChainedKafkaTransactionManager<>(kafkaTransactionManager, jpaTransactionManager);
        return transactionManager;
    }

    @Bean(name = "txKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory,
            ChainedKafkaTransactionManager<Object, Object> chainedTM) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        factory.getContainerProperties().setTransactionManager(chainedTM);
        factory.setAfterRollbackProcessor(new DefaultAfterRollbackProcessor((record, exception) -> {
        }, new FixedBackOff(0L, 0L)));
        return factory;
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return jpaTransactionManager(entityManagerFactory);
    }
}
