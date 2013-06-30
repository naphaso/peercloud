package org.peercloud.network2.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/26/13
 * Time: 3:37 PM
 */
public class RoutingTable implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RoutingTable.class);

    @Autowired
    private Timer timer;
    private TimerTask timeoutWatcher = new TimerTask() {
        @Override
        public void run() {
            for(Map.Entry<LinkID, RouteAction> link : routingMap.entrySet())
                if(link.getValue().isExpired())
                    delRoute(link.getKey());
        }
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        timer.schedule(timeoutWatcher, 0, 60*1000); // each 1 min
    }

    @Override
    public void destroy() throws Exception {
        timeoutWatcher.cancel();
    }


    private ConcurrentHashMap<LinkID, RouteAction> routingMap = new ConcurrentHashMap<>();


    public RouteAction route(LinkID linkID) {
        return routingMap.get(linkID);
    }

    public void addRoute(LinkID linkID, RouteAction action) {
        routingMap.put(linkID, action);
    }

    public void delRoute(LinkID linkID) {
        routingMap.remove(linkID);
    }
}
