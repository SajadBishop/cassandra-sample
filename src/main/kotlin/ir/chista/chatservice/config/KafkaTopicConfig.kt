package ir.chista.chatservice.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin;

@ConditionalOnProperty("kafka.enabled", matchIfMissing = true)
@Configuration
class KafkaTopicConfig(
    @Value(value = "\${spring.kafka.bootstrap-servers}")
    val bootstrapAddress: String) {

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = mutableMapOf<String, Any>()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }
    
    @Bean
    fun testTopic(): NewTopic {
         return TopicBuilder
             .name("testChatTopic")
             .replicas(1)
             .partitions(1)
             .build()
    }
}