package org.peercloud.network;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 9:18 AM
 */
public class F2FConnectionManager {
    private Logger logger = LoggerFactory.getLogger(F2FConnectionManager.class);
    static F2FConnectionManager instance;

    Map<Integer, F2FConnection> channelMap;

    private F2FConnectionManager() {
        channelMap = new HashMap<>();
    }

    public static synchronized F2FConnectionManager getInstance() {
        if(instance == null)
            instance = new F2FConnectionManager();
        return instance;
    }

    public void registerChannel(Channel channel, boolean client) {
        logger.debug("register channel with id {}, remote address {}", channel.id(), channel.remoteAddress());
        channelMap.put(channel.id(), new F2FConnection(channel, client));
    }

    public void unregisterChannel(Channel channel) {
        logger.debug("unregister channel with id {}, remote address {}", channel.id(), channel.remoteAddress());
        if(channelMap.containsKey(channel.id())) {
            channelMap.get(channel.id()).handleUnregister();
            channelMap.remove(channel.id());
        } else {
            logger.warn("unregister non-existing channel, id {}", channel.id());
        }
    }

    public F2FConnection getConnection(Integer id) {
        logger.debug("providing F2F connection for id {}", id);
        return channelMap.get(id);
    }
}
