package org.peercloud.network.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/1/13
 * Time: 1:09 AM
 */
public class ControlServer implements InitializingBean, DisposableBean, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ControlServer.class);

    private ServerSocket serverSocket;
    private Thread serverThread;


    private ThreadPoolExecutor threadPoolExecutor;
    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }



    private int port;
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        serverThread = new Thread(this);
        serverThread.setName("ControlServer");
        serverThread.start();
    }

    @Override
    public void destroy() throws Exception {
        threadPoolExecutor.shutdown();
        serverSocket.close();
        serverSocket = null;
    }

    @Override
    public void run() {
        logger.debug("starting control server...");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.error("can't bind to port {}", port);
            return;
        }
        while(serverThread != null) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                logger.debug("new client {}", clientSocket.getRemoteSocketAddress());
                threadPoolExecutor.execute(new ControlHandler(clientSocket));
            } catch (IOException e) {
                logger.error("error in accept call", e);
            }
        }
    }
}
