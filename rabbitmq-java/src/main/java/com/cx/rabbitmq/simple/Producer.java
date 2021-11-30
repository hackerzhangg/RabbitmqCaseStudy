package com.cx.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zgp
 * @Since 2021 -11 -14 21 :44
 * @Description 生产者
 */
public class Producer {

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
            connection = connectionFactory.newConnection("生成者");
            // 3: 通过连接获取通道Channel
            channel = connection.createChannel();
            // 4: 通过通创建交换机，声明队列，绑定关系，路由key，发送消息，和接收消息
            String queueName = "queue1";

            /*
             * @params1 队列的名称
             * @params2 是否要持久化durable=false 所谓持久化消息是否存盘，如果false 非持久化 true是持久化? 非持久化会存盘吗？ 会存盘，但是会随从重启服务会丢失。
             * @params3 排他性，是否是独占独立
             * @params4 是否自动删除，随着最后一个消费者消息完毕消息以后是否把队列自动删除
             * @params5 携带附属参数
             */
            channel.queueDeclare(queueName, true, false, false, null);
            // 5: 准备消息内容
            String message = "hello rabbitmq!";
            // 6: 发送消息给队列queue
            // @params1: 交换机  @params2 队列、路由key @params 消息的状态控制  @params4 消息主题
            // 面试题：可以存在没有交换机的队列吗？不可能，虽然没有指定交换机但是一定会存在一个默认的交换机。
            channel.basicPublish("", queueName, null, message.getBytes());

            System.out.println("消息发送成功!!!");
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
