package com.sl.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RecvMQ {
    private final static String QUEUE = "helloworld";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ 躲在的服务器的ip 和端口
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE, true, false, false, null);
        // 定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             * 消费者接受消息调用方法
             * param1:消费者的标签，在channel.basicConsume() 去指定
             * param2:消费包的内容，可从中获取消息id，消息routingKey，交换机，消息和重传标志（收到消息失败后是否需要重新发送）
             * param3:-
             * param4:-
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                // 交换机
                String exchange = envelope.getExchange();
                // 路由key
                String routingKey = envelope.getRoutingKey();
                // 消息id
                long deliveryTag = envelope.getDeliveryTag();
                // 消息内容
                String msg = new String(body, "UTF-8");
                System.out.println("receive message.." + msg);
            }
        };
        /**
         * 监听队列
         * param1:队列名称
         * param2:是否自动回复，设置为true为标识消息家受到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
         * param3:消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE, true, consumer);
    }
}
