package omar.mebarki.kafkatx.kafka.listener;

import omar.mebarki.kafkatx.domain.Message;
import omar.mebarki.kafkatx.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageListener {
    private final MessageRepository messageRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public MessageListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(id = "group1", topics = "topic1", containerFactory = "txKafkaListenerContainerFactory")
    @Transactional(transactionManager = "chainedTm")
    public void listen1(String in) {
        System.out.println("......" + in);

        Message msg = new Message();
        msg.setMessage(in);

        messageRepository.save(msg);
        System.out.println("saved");

        kafkaTemplate.send("topic2", in.toUpperCase());
        System.out.println("sent!");
    }

    @KafkaListener(id = "fooGroup3", topics = "topic2")
    public void listen(String in) {
        System.out.println("***************** ********** Received: " + in);
    }

}
