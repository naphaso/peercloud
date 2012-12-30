package org.peercloud.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.peercloud.network.ServerHandler;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/23/12
 * Time: 5:45 AM
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
// Enable stream compression (you can remove these two if unnecessary)
        //pipeline.addLast("deflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //pipeline.addLast("inflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
// Add the number codec first,
        //pipeline.addLast("decoder", new BigIntegerDecoder());
        //pipeline.addLast("encoder", new NumberEncoder());
// and then business logic.
// Please note we create a handler for every new channel
// because it has stateful properties.
        pipeline.addLast("handler", new ServerHandler());
    }
}