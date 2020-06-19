package omar.mebarki.kafkatx.kafka.listener;

import omar.mebarki.kafkatx.domain.Message;
import omar.mebarki.kafkatx.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageListener {
    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageListener(MessageRepository messageRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "group1", topics = "topic1")
    @Transactional(transactionManager = "chainedTm")
    public void listen1(String in) {
        System.out.println("......" + in);

        Message msg = new Message();
        msg.setMessage(in);

        messageRepository.save(msg);
        System.out.println("saved");

        kafkaTemplate.executeInTransaction(t -> t.send("topic2", in.toUpperCase()));
        System.out.println("sent!");

    }

}
