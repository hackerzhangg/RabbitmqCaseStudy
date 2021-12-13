package com.cx.rabbitmq.simple;

import com.rabbitmq.client.*;
import java.io.IOException;

/**
 * @Author zgp
 * @Since 2021 -11 -14 21 :44
 * @Description 消费者
 */
public class Consumer {

    public static void main(String[] args) {

        // 所有的中间件技术都是基于tcp/ip协议基础之上构建新型的协议规范，只不过rabbitmq遵循的是amqp
        // ip port
        // 1: 创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //ip地址
        connectionFactory.setHost("127.0.0.1");
        //端口号
        connectionFactory.setPort(5672);
        //用户名
        connectionFactory.setUsername("guest");
        //用户密码
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            // 2: 创建连接Connection Rabbitmq为什么是基于channel去处理而不是链接? 长连接----信道channel
            connection = connectionFactory.newConnection("生成者1");
            // 3: 通过连接获取通道Channel
            channel = connection.createChannel();
            // 4: 通过通创建交换机，声明队列，绑定关系，路由key，发送消息，和接收消息

            channel.basicConsume("queue1", true, new DeliverCallback() {
                public void handle(String s, Delivery delivery) throws IOException {
                    System.out.println("收到的消息是"+new String(delivery.getBody(),"UTF-8"));
                }
            }, new CancelCallback() {
                public void handle(String s) throws IOException {
                    System.out.println("接收消息失败了!");
                }
            });
            System.out.println("消息发送成功!!!");
            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 7: 关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // 8: 关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
