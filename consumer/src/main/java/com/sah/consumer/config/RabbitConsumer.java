package com.sah.consumer.config;

import com.rabbitmq.client.Channel;
import com.sah.common.common.Constant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Date;

/**
 * @author suahe
 * @date 2022/2/24
 * @ApiNote
 */
@Configuration
public class RabbitConsumer {

    @RabbitHandler
    @RabbitListener(queues = Constant.TEST_QUEUE)
    public void receiveMessage1(String str, Channel channel, Message message) throws IOException {
        System.out.println(new Date().toLocaleString()+"我接受到了消息：" + str);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = Constant.DIRECT_QUEUE,
                            durable = "true"),
                    exchange = @Exchange(value =Constant.DIRECT_EXCHANGE),
                    key = Constant.DIRECT_QUEUE
            )
    )
    public void receiveMessage2(String str, Channel channel, Message message) {
        try {
            System.out.println("处理消息成功:" + str);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *   路由key为 topic.aa
     */
    @RabbitListener(queues = Constant.TOPIC_QUEUE_1)
    @RabbitHandler
    public void receiveMessage3(String str, Channel channel, Message message){
        try {
            System.out.println("处理消息成功:" + str);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //回复ack
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *   路由key为 topic.bb
     */
    @RabbitListener(queues = Constant.TOPIC_QUEUE_2)
    @RabbitHandler
    public void receiveMessage4(String str, Channel channel, Message message){

        try {
            System.out.println("处理消息成功:" + str);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //回复ack
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     *   路由key为 topic.#
     * @param message
     */
    @RabbitListener(queues = Constant.TOPIC_QUEUE_3)
    @RabbitHandler
    public void receiveMessage5(String str, Channel channel, Message message) {
        System.out.println("处理消息成功:" + str);
    }
}
