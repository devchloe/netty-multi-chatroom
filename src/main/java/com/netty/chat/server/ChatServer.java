package com.netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class ChatServer {

    private final int port;
    private final String host;


    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {

        new ChatServer("node01", 8081).run();
    }

    public void run() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitializer());

            ChannelFuture future = sb.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

//        getKafkaConsumer().subscribe(Arrays.asList("chatroom02"));
    }

//    public KafkaConsumer getKafkaConsumer() {
//        Properties configs = new Properties();
//        configs.put("bootstrap.servers", "localhost:9092");
//        configs.put("session.timeout.ms", "10000");
//        configs.put("group.id", "chatroom02");
//        configs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
//        return consumer;
//    }
}
