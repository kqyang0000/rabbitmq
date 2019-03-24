package com.sl.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SendMQ {
    private final static String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        // 默认端口号
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello world";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x]Sent " + message);
        channel.close();
        connection.close();
    }
}
