package org.peercloud;

import org.peercloud.network.Server;

public class App {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
