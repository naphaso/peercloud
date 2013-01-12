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
 * Time: 9:02 AM
 */
public class InitState implements State {
    static private Logger logger = LoggerFactory.getLogger(InitState.class);
    F2FConnection connection;
    AsyncXMLStreamReader reader;

    public InitState(F2FConnection connection, AsyncXMLStreamReader reader) {
        logger.debug("InitState for connection id {}", connection.getChannel().id());
        this.connection = connection;
        this.reader = reader;

        connection.getChannel().write("<stream version=\"1.0\">\n");
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
                        if (reader.getLocalName().equals("stream")) {
                            if(!reader.getAttributeValue("", "version").equals("1.0")) {
                                // TODO: version compatibility code
                            }
                            IDState state = new IDState(connection, reader);
                            connection.pushState(state);
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
