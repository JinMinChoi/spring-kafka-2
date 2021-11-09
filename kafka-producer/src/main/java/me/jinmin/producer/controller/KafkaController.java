package me.jinmin.producer.controller;

import lombok.RequiredArgsConstructor;
import me.jinmin.producer.domain.PushEntity;
import me.jinmin.producer.service.KafkaMessageSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaMessageSender kafkaMessageSender;

    @PostMapping("/kafka")
    public String send(@RequestBody PushEntity pushEntity) {
        kafkaMessageSender.send(pushEntity);
        return "Successful Sending!!!";
    }
}
