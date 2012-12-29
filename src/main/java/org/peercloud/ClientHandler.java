package org.peercloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandler;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/26/12
 * Time: 10:46 PM
 */
public class ClientHandler extends ChannelInboundByteHandlerAdapter {


    @Override
    public void inboundBufferUpdated(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    }
}
