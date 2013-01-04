package org.peercloud.xml;

import com.fasterxml.aalto.AsyncXMLStreamReader;
import org.peercloud.network.F2FConnection;
import org.peercloud.network.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 10:15 AM
 */
public class IDState implements State {
    static Logger logger = LoggerFactory.getLogger(IDState.class);

    F2FConnection connection;
    AsyncXMLStreamReader reader;
    boolean in_id;
    StringBuilder certBuilder = new StringBuilder();

    public IDState(F2FConnection connection, AsyncXMLStreamReader reader) {
        this.connection = connection;
        this.reader = reader;
        in_id = false;

        if(connection.isClient())
            connection.getChannel().write("<id>" +
                    Server.getInstance().getServerCertificate().serialize(false) +
                    "</id>\n");
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
                        if (reader.getLocalName().equals("id")) {
                            in_id = true;
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;
                    case AsyncXMLStreamReader.END_ELEMENT:
                        String elementName = reader.getLocalName();
                        if(elementName.equals("id")) {
                            if(in_id) {
                                if(!connection.isClient()) {
                                    // TODO: check certificate (is friend?)
                                    connection.getChannel().write("<id>" +
                                            Server.getInstance().getServerCertificate().serialize(false) +
                                            "</id>\n");
                                }
                                AuthState state = new AuthState(connection, reader);
                                connection.pushState(state);
                                state.handle();
                            }
                        } else if(elementName.equals("stream")) {
                            logger.debug("stream ended, F2F connection closed");
                            connection.close();
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;
                    case AsyncXMLStreamReader.CHARACTERS:
                        if(in_id)
                            certBuilder.append(reader.getTextCharacters());
                        break;

                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML stream exception", e);
        }
    }
}
