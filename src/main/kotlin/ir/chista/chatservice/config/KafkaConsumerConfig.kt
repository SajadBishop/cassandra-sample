package ir.chista.chatservice.config;

import ir.chista.chatservice.TestKafkaListener
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@ConditionalOnProperty("kafka.enabled", matchIfMissing = true)
@Configuration
class KafkaConsumerConfig(val kafkaProperties: KafkaProperties) {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val deserializer = JsonDeserializer(Any::class.java)
        deserializer.addTrustedPackages("*")
        val props = mutableMapOf<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = kafkaProperties.consumer.groupId
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), deserializer)
    }

    @Bean
    @Primary
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    @Bean
    fun filterKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        factory.setRecordFilterStrategy { record: ConsumerRecord<String?, String> ->
            record.value().contains("World")
        }
        return factory
    }

    @Bean
    fun testKafkaListener(): TestKafkaListener {
        return TestKafkaListener()
    }
}