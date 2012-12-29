package org.peercloud;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/23/12
 * Time: 4:01 AM
 */
public class Server {
    static final Logger logger = LoggerFactory.getLogger(Server.class);

    private Config config;

    public Server() {
        logger.info("starting server...");
        config = new Config();
        try {
            config.load("server.conf");
        } catch (FileNotFoundException e) {
            logger.error("configuration file not found");
            System.exit(1);
        }


    }

    public void run() {
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .localAddress(new InetSocketAddress(31337))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                            //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer());

            // Start the server.

            ChannelFuture f = b.bind().sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("server interrupted: {}", e.getMessage());
        } finally {
            // Shut down all event loops to terminate all threads.
            b.shutdown();
        }
    }
}
