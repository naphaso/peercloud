package org.peercloud.xml;

import com.fasterxml.aalto.AsyncXMLStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class InitState implements State {
    AsyncXMLStreamReader reader;
    public InitState(AsyncXMLStreamReader reader) {
        this.reader = reader;
    }

    @Override
    public void handle() {

    }
}
