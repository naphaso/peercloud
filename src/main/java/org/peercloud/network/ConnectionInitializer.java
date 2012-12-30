package org.peercloud.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.peercloud.network.ClientHandler;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/26/12
 * Time: 10:44 PM
 */
public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        // stream compression
        //channelPipeline.addLast("deflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //channelPipeline.addLast("inflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // protocol codec
        //channelPipeline.addLast("decoder", new JsonProtocolDecoder());
        //channelPipeline.addLast("encoder", new JsonProtocolEncoder());

        // business logic handler
        channelPipeline.addLast("handler", new ClientHandler());

    }
}
