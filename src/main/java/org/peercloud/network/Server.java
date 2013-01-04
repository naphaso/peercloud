package org.peercloud.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.peercloud.Config;
import org.peercloud.crypto.Certificate;
import org.peercloud.crypto.CertificateStorage;
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
    private static Server instance;

    private Config config;

    private Server() {
        logger.info("starting server...");
        config = new Config();
        try {
            config.load("/home/wolong/repo/peercloud/org.peercloud/server.conf");
        } catch (FileNotFoundException e) {
            logger.error("configuration file not found");
            System.exit(1);
        }
    }

    public static synchronized Server getInstance() {
        if(instance == null)
            instance = new Server();
        return instance;
    }

    public Certificate getServerCertificate() {
        // TODO: move fingerprint to config variable
        return CertificateStorage.getInstance().getCertificateByFingerprint("fa8db57cee488d29a9310183376e1c08f44f35d4");
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
