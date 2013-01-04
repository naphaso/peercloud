package org.peercloud.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundMessageHandlerAdapter<String> {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);



    public ServerHandler() {

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String in) throws Exception {
        F2FConnection connection = F2FConnectionManager.getInstance().getConnection(ctx.channel().id());
        connection.handleData(in);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.warn("Unexpected exception from downstream", cause);
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        F2FConnectionManager.getInstance().registerChannel(ctx.channel(), false);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        F2FConnectionManager.getInstance().unregisterChannel(ctx.channel());
    }
}
