package org.peercloud.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ChannelBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/30/13
 * Time: 12:17 AM
 */
public class NetworkMessageDecoder extends ByteToMessageCodec<NetworkMessage, NetworkMessage> {
    private static final Logger logger = LoggerFactory.getLogger(NetworkMessageDecoder.class);
    private Map<Integer, ConnectionContext> connectionContextMap = new HashMap<Integer, ConnectionContext>();

    @Override
    public void encode(ChannelHandlerContext ctx, NetworkMessage msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getData().length);
        out.writeBytes(msg.getData());
    }

    @Override
    public NetworkMessage decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Integer id = ctx.channel().id();
        ConnectionContext context = connectionContextMap.get(id);
        if(context == null) {
            logger.debug("new channel context");
            context = new ConnectionContext();
            connectionContextMap.put(id, context);
            //context.xorKey = context.buffer.readByte();
        }

        context.buffer.writeBytes(msg);

        if(context.buffer.readableBytes() >= 4) {
            int size = context.buffer.getInt(0);
            logger.debug("size = {}, readableBytes = {}", size, context.buffer.readableBytes());
            if(context.buffer.readableBytes() >= size + 4) {
                context.buffer.skipBytes(4);
                byte[] messageBuffer = new byte[size];
                context.buffer.readBytes(messageBuffer);
                return new NetworkMessage(messageBuffer);
            }
        }

        return null;
    }

    @Override
    public void freeInboundBuffer(ChannelHandlerContext ctx, ChannelBuf buf) throws Exception {
    }

    @Override
    public void freeOutboundBuffer(ChannelHandlerContext ctx, ChannelBuf buf) throws Exception {
    }

    private class ConnectionContext {
        public ByteBuf buffer = Unpooled.buffer(1500);
        //public byte xorKey;
    }
}
