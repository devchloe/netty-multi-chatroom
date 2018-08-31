package com.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoomProxyHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, ChannelGroup> CHANNEL_GROUPS;
    private final Map<Integer, String> CHANNEL_TO_ROOM;


    private static final String ROOM_SELECTION_COMMAND = "/chatroom";
    final AttributeKey<String> roomNumber = AttributeKey.newInstance("roomNumber");
    final AttributeKey<Integer> channelId = AttributeKey.newInstance("channelId");
    private static final AtomicInteger count = new AtomicInteger(0);


    public ChatRoomProxyHandler(Map<String, ChannelGroup> channelGroups, Map<Integer, String> channelToRoom) {
        this.CHANNEL_GROUPS = channelGroups;
        this.CHANNEL_TO_ROOM = channelToRoom;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        int value = count.incrementAndGet();
        ctx.channel().attr(channelId).set(value);

        incoming.writeAndFlush("Input /chatroom [roomName]" + "\r\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel incoming = ctx.channel();

        String roomSelectionMsg = (String) msg;
        String roomName = "";
        if (((String) msg).contains(ROOM_SELECTION_COMMAND)) {
            roomName = roomSelectionMsg.substring(ROOM_SELECTION_COMMAND.length() + 1);
        } else {
            ctx.fireChannelRead(msg);
        }

        if (!CHANNEL_GROUPS.containsKey(roomName)) {
            CHANNEL_GROUPS.put(roomName, new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE));
        }

        CHANNEL_GROUPS.get(roomName).add(incoming);
        incoming.attr(roomNumber).set(roomName);

        CHANNEL_TO_ROOM.put(incoming.attr(channelId).get(), roomName);

        incoming.writeAndFlush("You've joined ROOM NAME: " + roomName + "\r\n");

//        ctx.fireChannelRead(msg)
    }

    public ChannelGroup getChannelGroupBy(String roomName) {
        if (!CHANNEL_GROUPS.containsKey(roomName))
            throw new IllegalArgumentException("Cannot find ROOM NAME: " + roomName);
        return CHANNEL_GROUPS.get(roomName);
    }
}
