package com.netty.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Map<String, ChannelGroup> CHANNEL_GROUPS = new ConcurrentHashMap<>(100);
    private final Map<Integer, String> CHANNEL_TO_ROOM = new ConcurrentHashMap<>(100);


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("roomProxy", new ChatRoomProxyHandler(CHANNEL_GROUPS, CHANNEL_TO_ROOM));
        // proxy handler
            // if connected, show /create [roomNumber]
            // if roomNumber가 roomList에 있으면 roomList에 해당하는 ChannelGroup에 추가
            // else roomList에 roomNumber를 추가하고 rootList에 해당하는 ChannelGroup에 추가
//        pipeline.addLast("handler", new ChatServerHandler(CHANNEL_GROUPS));
//        pipeline.addLast("chatRoomMessageConsumingHandler", new ChatRoomMessageConsumingHandler());
    }
}
