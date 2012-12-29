package org.peercloud.xml;

import com.fasterxml.aalto.AsyncXMLStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class PeercloudXMLHandler {
    protected AsyncXMLStreamReader reader;
    protected State state;
    public PeercloudXMLHandler(AsyncXMLStreamReader reader) {
        this.reader = reader;
    }

    public void handle() {
        state.handle();
    }
}
