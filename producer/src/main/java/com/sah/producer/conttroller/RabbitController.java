package com.sah.producer.conttroller;

import com.sah.common.common.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

/**
 * @author suahe
 * @date 2022/2/24
 * @ApiNote
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage")
    public void send() {
        String message =  "生产者发送消息" + new Date();
        System.out.println(message);
        rabbitTemplate.convertAndSend(Constant.TEST_EXCHANGE , "", message);
    }
}
