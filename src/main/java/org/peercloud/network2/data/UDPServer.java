package org.peercloud.network2.data;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetSocketAddress;


/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/26/13
 * Time: 2:41 PM
 */
public class UDPServer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);
    private int port = 9091;

    public void setPacketHandler(ChannelInboundMessageHandlerAdapter<DatagramPacket> packetHandler) {
        this.packetHandler = packetHandler;
    }

    ChannelInboundMessageHandlerAdapter<DatagramPacket> packetHandler;

    public void setThreadPool(NioEventLoopGroup threadPool) {
        this.threadPool = threadPool;
    }

    NioEventLoopGroup threadPool;

    public UDPServer() {

    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("starting UDPServer...");
        Bootstrap b = new Bootstrap();
        try {
            b.group(threadPool)
                    .channel(NioDatagramChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .handler(packetHandler);


            b.bind().sync();//.channel().closeFuture().await();
        } catch (InterruptedException e) {
            logger.error("server interrupted");
        }/* finally {
            b.shutdown();
        }*/
    }
}
