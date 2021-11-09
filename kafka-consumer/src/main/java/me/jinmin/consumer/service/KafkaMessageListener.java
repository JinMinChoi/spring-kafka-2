package me.jinmin.consumer.service;

import me.jinmin.consumer.domain.PushEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    @KafkaListener(
            topics = "${kafka.topic.name}",
            groupId = "${kafka.group.name}",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void listenWithHeaders(
            @Payload PushEntity pushEntity,
            @Headers MessageHeaders messageHeaders) {
        System.out.println(String.format("Received Message: %s with headres: %s", pushEntity.toString(), messageHeaders));
    }
}
