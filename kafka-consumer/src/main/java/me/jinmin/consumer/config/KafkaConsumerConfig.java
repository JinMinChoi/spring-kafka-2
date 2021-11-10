package me.jinmin.consumer.config;

import me.jinmin.consumer.domain.PushEntity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${kafka.group.name}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, PushEntity> consumerFactory() {
        JsonDeserializer<PushEntity> deserializer = pushEntityJsonDeserializer();
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), deserializer);
    }

    private JsonDeserializer<PushEntity> pushEntityJsonDeserializer() {
        JsonDeserializer<PushEntity> deserializer = new JsonDeserializer<>();
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        return deserializer;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PushEntity> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PushEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
