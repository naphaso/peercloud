package org.peercloud.xml;

import com.fasterxml.aalto.AsyncXMLStreamReader;
import org.peercloud.network.F2FConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 1/15/13
 * Time: 3:48 AM
 */

public class SessionState implements State {
    private static Logger logger = LoggerFactory.getLogger(SessionState.class);

    F2FConnection connection;
    AsyncXMLStreamReader reader;

    public SessionState(F2FConnection connection, AsyncXMLStreamReader reader) {
        this.connection = connection;
        this.reader = reader;
    }

    private enum ParserState {
        VOID, MESSAGE
    }

    private ParserState state = ParserState.VOID;
    private StringBuilder data;

    @Override
    public void handle() {
        int type;
        try {
            while (true) {
                type = reader.next();

                if(type == AsyncXMLStreamReader.EVENT_INCOMPLETE) break;
                switch (type) {

                    case AsyncXMLStreamReader.START_ELEMENT:
                        switch (state) {
                            case VOID:
                                switch (reader.getLocalName()) {
                                    case "message":
                                        state = ParserState.MESSAGE;
                                        data = new StringBuilder();
                                }
                                break;
                        }
                        break;


                    case AsyncXMLStreamReader.END_ELEMENT:
                        switch (state) {
                            case MESSAGE:
                                switch (reader.getLocalName()) {
                                    case "message":
                                        String message = data.toString();
                                        state = ParserState.VOID;
                                        logger.debug("message: {}", message);
                                        break;
                                }
                                break;
                        }
                        break;


                    case AsyncXMLStreamReader.CHARACTERS:
                        switch (state) {
                            case MESSAGE:
                                data.append(reader.getText());
                                break;
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML stream exception", e);
        }
    }
}
