package org.peercloud.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ChannelBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/30/13
 * Time: 12:02 AM
 */
public class ConnectionHandler extends ChannelInboundMessageHandlerAdapter<NetworkMessage> {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private ConnectionMode connectionMode;
    public ConnectionHandler(ConnectionMode connectionMode) {
        this.connectionMode = connectionMode;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if(connectionMode == ConnectionMode.CLIENT) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < 1000000; i++) {
                stringBuilder.append(Integer.toString(i));
                stringBuilder.append(',');
            }
           ctx.write(new NetworkMessage(stringBuilder.toString().getBytes()));
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, NetworkMessage msg) throws Exception {
        String message = new String(msg.getData());
        //logger.info("message: {}", message);
    }
}
