package org.peercloud.network2.data;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/26/13
 * Time: 4:16 PM
 */
public class RouteAction {
    private static final long timeout = 3600 * 1000; // one hour
    private InetSocketAddress direction;
    private long lastActionTime = new Date().getTime();
    public RouteAction(InetSocketAddress direction) {
        this.direction = direction;
    }

    public boolean isExpired() {
        return new Date().getTime() - lastActionTime > timeout;
    }

    public void touch() {
        lastActionTime = new Date().getTime();
    }

    public InetSocketAddress getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return direction.toString();
    }
}
