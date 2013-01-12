package org.peercloud.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class ConnectionHandler extends ChannelInboundMessageHandlerAdapter<String> {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private boolean client;

    public ConnectionHandler(boolean client) {
        this.client = client;
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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        F2FConnectionManager.getInstance().registerChannel(ctx.channel(), client);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        F2FConnectionManager.getInstance().unregisterChannel(ctx.channel());
    }
}
