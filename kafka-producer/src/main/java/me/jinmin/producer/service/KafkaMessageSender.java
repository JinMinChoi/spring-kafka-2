package me.jinmin.producer.service;

import lombok.RequiredArgsConstructor;
import me.jinmin.producer.domain.PushEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final KafkaTemplate<String, PushEntity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public void send(PushEntity pushEntity) {
        Message<PushEntity> message = MessageBuilder
                .withPayload(pushEntity)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        ListenableFuture<SendResult<String, PushEntity>> future = kafkaTemplate.send(message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, PushEntity> result) {
                System.out.println(String.format("Sent message = [ %s ] with offset = [ %d ] ", result.getProducerRecord().value().toString(), result.getRecordMetadata().offset()));
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println(String.format("Unable to send message=[] due to : %s", ex.getMessage()));
            }
        });
    }
}
