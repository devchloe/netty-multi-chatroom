package com.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.util.Map;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    //    private static final ChannelGroup channels = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final Map<String, ChannelGroup> CHANNEL_GROUPS;
    public ChatServerHandler(Map<String, ChannelGroup> channelGroups) {
        this.CHANNEL_GROUPS = channelGroups;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        String roomName = getRoomNameOf(incoming);

        incoming.writeAndFlush("[SERVER] - " + "WELCOME TO THIS SIMPLE CHAT APP!" + incoming.remoteAddress() + "\r\n");

        ChannelGroup channels = CHANNEL_GROUPS.get(roomName);

        for (Channel channel : channels) {
            channels.write("[SERVER] - " + incoming.remoteAddress() + " has joined\r\n");
        }
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
//        String roomName = getRoomNameOf(incoming);
//        ChannelGroup channels = null;
//
//        if (roomName != null) {
//            channels = CHANNEL_GROUPS.remove(roomName);
//        }

//        for (Channel channel : channels) {
//            channel.write("[SERVER] - " + incoming.remoteAddress() + " has left\r\n");
//        }
//        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = ctx.channel();

        String roomName = getRoomNameOf(incoming);

        ChannelGroup channels = null;

        if (roomName != null) {
            channels = CHANNEL_GROUPS.get(roomName);
        }

        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\r\n");
            } else {
                channel.writeAndFlush("[you] " + msg + "\r\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public String getRoomNameOf(Channel channel) {
        String roomName = "";
        if (AttributeKey.exists("roomNumber"))
            roomName = (String) channel.attr(AttributeKey.valueOf("roomNumber")).get();

        return roomName;
    }
}
