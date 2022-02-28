package com.sah.producer.config;

import com.sah.common.common.Constant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author suahe
 * @date 2022/2/24
 * @ApiNote
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //fanout方式
    @Bean(name=Constant.TEST_EXCHANGE)
    public FanoutExchange testExchange(){
        return new FanoutExchange(Constant.TEST_EXCHANGE, true, false);
    }

    @Bean(name=Constant.TEST_QUEUE)
    public Queue testQueue(){
        return new Queue(Constant.TEST_QUEUE, true, false, false);
    }

    @Bean
    public Binding bindQueue(
            @Qualifier(value=Constant.TEST_EXCHANGE) FanoutExchange  exchange,
            @Qualifier(value=Constant.TEST_QUEUE) Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }

    //direct方式
    @Bean(name = Constant.DIRECT_QUEUE)
    public Queue directQueue(){
        return new Queue(Constant.DIRECT_QUEUE);
    }

    @Bean(name = Constant.DIRECT_EXCHANGE)
    public DirectExchange directExchange(){
        return new DirectExchange(Constant.DIRECT_EXCHANGE);

    }
    @Bean(name = Constant.DIRECT_EXCHANGE+Constant.DOT+Constant.DIRECT_QUEUE)
    public Binding  directBinding(){
        Queue queue= directQueue();
        DirectExchange exchange= directExchange();
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    //topic方式
    @Bean(name = Constant.TOPIC_QUEUE_1)
    public Queue topicQueue1(){
        return new Queue(Constant.TOPIC_QUEUE_1);
    }

    @Bean(name = Constant.TOPIC_QUEUE_2)
    public Queue topicQueue2(){
        return new Queue(Constant.TOPIC_QUEUE_2);
    }

    @Bean(name = Constant.TOPIC_QUEUE_3)
    public Queue topicQueue3(){
        return new Queue(Constant.TOPIC_QUEUE_3);
    }


    @Bean(name = Constant.TOPIC_EXCHANGE)
    public TopicExchange topicExchange(){
        return new TopicExchange(Constant.TOPIC_EXCHANGE);
    }


    @Bean
    public Binding topicBinding1(){
        Queue queue= topicQueue1();
        TopicExchange exchange= topicExchange();
        return BindingBuilder.bind(queue).to(exchange).with(Constant.TOPIC_ROUTING_KEY_1);
    }
    @Bean
    public Binding topicBinding2(){
        Queue queue= topicQueue2();
        TopicExchange exchange= topicExchange();
        return BindingBuilder.bind(queue).to(exchange).with(Constant.TOPIC_ROUTING_KEY_2);
    }
    @Bean
    public Binding topicBinding3(){
        Queue queue= topicQueue3();
        TopicExchange exchange= topicExchange();
        return BindingBuilder.bind(queue).to(exchange).with(Constant.TOPIC_ROUTING_KEY_3);
    }

    @PostConstruct
    public void adviceRabbitmqTemplate(){
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
            if(ack){
                System.out.println("消息发送成功！！！");
            }else{
                System.out.println("消息发送失败！！！");
            }
        });
    }
}
