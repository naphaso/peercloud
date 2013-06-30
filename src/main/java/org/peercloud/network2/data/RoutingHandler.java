package org.peercloud.network2.data;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/26/13
 * Time: 2:47 PM
 */
public class RoutingHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {
    private static final Logger logger = LoggerFactory.getLogger(RoutingHandler.class);
    @Autowired
    private RoutingTable routingTable;

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        ByteBuf data = datagramPacket.data();
        byte[] link_id = new byte[10];
        data.getBytes(0, link_id);
        LinkID linkID = new LinkID(link_id);
        RouteAction routeAction = routingTable.route(linkID);
        logger.debug("route link {} to {}", linkID, routeAction);
        if(routeAction != null)
            channelHandlerContext.write(new DatagramPacket(data, routeAction.getDirection()));
    }
}
