package omar.mebarki.kafkatx.repository;

import omar.mebarki.kafkatx.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}
