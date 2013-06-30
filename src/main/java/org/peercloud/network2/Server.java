package org.peercloud.network2;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.peercloud.Config;
import org.peercloud.FriendManager;
import org.peercloud.crypto.Certificate;
import org.peercloud.crypto.CertificateStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/23/12
 * Time: 4:01 AM
 */
public class Server {
    static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static Server instance;

    public Config getConfig() {
        return config;
    }

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

    private static final String serverCertificateFingerprint = "ccd92c8d94aedf0f9fd150c5b5b5e0e6e3ea09bb";
    public Certificate getServerCertificate() {
        // TODO: move fingerprint to config variable
        Certificate certificate = CertificateStorage.getInstance().getCertificateByFingerprint(serverCertificateFingerprint);
        if(certificate == null)
            logger.error("Not found server certificate with fingerprint {}", serverCertificateFingerprint);
        return certificate;
    }

    public void run() {
        ServerBootstrap b = new ServerBootstrap();
        try {
            NioEventLoopGroup bossExec   = new NioEventLoopGroup();
            NioEventLoopGroup ioExec     = new NioEventLoopGroup();
            NioEventLoopGroup clientExec = new NioEventLoopGroup();

            b.group(bossExec, ioExec)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .localAddress(new InetSocketAddress(31337))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                            //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ConnectionInitializer(false));

            // Start the server.
            logger.debug("Listening...");
            ChannelFuture f = b.bind().sync();


            FriendManager friendManager = FriendManager.getInstance();
            Random randomGenerator = new Random();
            while(true) {

                if(!friendManager.inNetwork()) {
                    logger.debug("Not in network. Pick random bootstrap address.");
                    List<InetAddress> inetAddresses = config.getBootstrap();
                    if(inetAddresses.isEmpty()) {
                        logger.error("Empty bootstrap.");
                        continue;
                    }
                    InetAddress bootstrapAddr = inetAddresses.get(randomGenerator.nextInt(inetAddresses.size()));

                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(clientExec)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .remoteAddress(bootstrapAddr, 31337)
                            .handler(new ConnectionInitializer(true));
                    logger.debug("Connecting to {}", inetAddresses);
                    try {
                        ChannelFuture channelFuture = bootstrap.connect().sync();
                        if(channelFuture.await(15, TimeUnit.SECONDS)) {
                            logger.debug("Connection to {} success", inetAddresses);
                        } else {
                            logger.debug("Connection to {} timeout", inetAddresses);
                        }
                    } catch (ChannelException e) {
                        logger.error("Connection failed to {}: {}", inetAddresses, e.getMessage());
                    }
                }

                Thread.sleep(15000);
            }

            // Wait until the server socket is closed.
            //f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("server interrupted: {}", e.getMessage());
        } finally {
            // Shut down all event loops to terminate all threads.
            b.shutdown();
        }
    }
}
