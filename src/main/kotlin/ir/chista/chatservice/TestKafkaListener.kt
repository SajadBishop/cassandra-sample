package ir.chista.chatservice

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload

//@Component
class TestKafkaListener {

    @KafkaListener(topics = ["testChatTopic"])
    fun listenWithHeaders(
        @Payload message: Any,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: String
    ) {
        println("Received Message: $message - from partition: $partition");
    }
}