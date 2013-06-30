package org.peercloud.network2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/26/12
 * Time: 7:37 PM
 */
public class Connection {
    private final String host;
    private final int port;

    public Connection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {


        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ConnectionInitializer(false));


            ChannelFuture channelFuture = bootstrap.connect().sync();
            //channelFuture.await(15, Time)
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bootstrap.shutdown();
        }
    }
}
