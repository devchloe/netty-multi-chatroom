//package com.netty.chat.server;
//
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.util.concurrent.ImmediateEventExecutor;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//
//public class ChatRoomMessageConsumingHandler extends SimpleChannelInboundHandler<ChatRoomMessage> {
//
//    private ChannelGroup channels = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRoomMessage chatRoomMessage) throws Exception {
//        while (true) {
//            ConsumerRecords<String, String> records = getKafkaConsumer().poll(500);
//            for (ConsumerRecord<String, String> record : records) {
//                switch (record.topic()) {
//                    case "chatroom02":
//                        System.out.println(record.value());
//                        for (Channel channel : channels) {
//                            channel.writeAndFlush(record.value());
//                        }
//                        break;
//                    default:
//                        throw new IllegalStateException("get message on topic " + record.topic());
//                }
//            }
//        }
//    }
//
//    private KafkaConsumer getKafkaConsumer() {
//        return getKafkaConsumer();
//    }
//}
