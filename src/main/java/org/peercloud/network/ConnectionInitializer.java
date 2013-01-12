package org.peercloud.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/23/12
 * Time: 5:45 AM
 */
public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {

    boolean client;

    public ConnectionInitializer(boolean client) {
        this.client = client;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
// Enable stream compression (you can remove these two if unnecessary)
        //pipeline.addLast("deflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //pipeline.addLast("inflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
// Add the number codec first,
        //pipeline.addLast("decoder", new BigIntegerDecoder());
        //pipeline.addLast("encoder", new NumberEncoder());
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));

        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

// and then business logic.
// Please note we create a handler for every new channel
// because it has stateful properties.

        pipeline.addLast("handler", new ConnectionHandler(client));
    }
}