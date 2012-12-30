package org.peercloud.xml;

import com.fasterxml.aalto.AsyncXMLStreamReader;
import org.peercloud.network.F2FConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 10:15 AM
 */
public class StreamUnauthState implements State {
    static Logger logger = LoggerFactory.getLogger(StreamUnauthState.class);

    F2FConnection connection;
    AsyncXMLStreamReader reader;
    InitState parent;

    public StreamUnauthState(F2FConnection connection, AsyncXMLStreamReader reader, InitState parent) {
        this.connection = connection;
        this.reader = reader;
        this.parent = parent;
    }


    @Override
    public void handle() {
        int type;
        try {
            while (true) {
                type = reader.next();
                if(type == AsyncXMLStreamReader.EVENT_INCOMPLETE) break;
                switch (type) {
                    case AsyncXMLStreamReader.START_ELEMENT:
                        if (reader.getLocalName().equals("auth")) {
                            AuthState state = new AuthState(connection, reader, this);
                            connection.setState(state);
                            state.handle();
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;
                    case AsyncXMLStreamReader.END_ELEMENT:
                        if(reader.getLocalName().equals("stream")) {
                            logger.debug("stream ended, F2F connection closed");
                            connection.close();
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;

                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML stream exception", e);
        }
    }
}
