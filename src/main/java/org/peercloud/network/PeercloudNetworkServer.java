package org.peercloud.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/29/13
 * Time: 11:54 PM
 */
@Component
public class PeercloudNetworkServer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(PeercloudNetworkServer.class);


    NioEventLoopGroup bossExec   = new NioEventLoopGroup();
    NioEventLoopGroup ioExec     = new NioEventLoopGroup();
    NioEventLoopGroup clientExec = new NioEventLoopGroup();


    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(bossExec, ioExec)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .localAddress(new InetSocketAddress(31337))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                            //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ConnectionInitializer(ConnectionMode.SERVER));

            // Start the server.
            logger.debug("Listening...");
            ChannelFuture f = b.bind().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect(String address) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientExec)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(address, 31337)
                .handler(new org.peercloud.network.ConnectionInitializer(ConnectionMode.CLIENT));
        logger.debug("Connecting to {}", address);
        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            if(channelFuture.await(15, TimeUnit.SECONDS)) {
                logger.debug("Connection to {} success", address);
            } else {
                logger.debug("Connection to {} timeout", address);
            }
        } catch (ChannelException e) {
            logger.error("Connection failed to {}: {}", address, e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Interrupted exception", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
        this.connect("127.0.0.1");
    }
}
