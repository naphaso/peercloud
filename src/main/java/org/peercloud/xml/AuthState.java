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
 * Time: 10:28 AM
 */
public class AuthState implements State {
    private static Logger logger = LoggerFactory.getLogger(AuthState.class);

    F2FConnection connection;
    AsyncXMLStreamReader reader;

    public AuthState(F2FConnection connection, AsyncXMLStreamReader reader) {
        this.connection = connection;
        this.reader = reader;
    }

    int within = 0;
    // TODO: remove simple user/password auth (it's for debug)
    StringBuilder user_sb = new StringBuilder();
    StringBuilder password_sb = new StringBuilder();
    String user;
    String password;

    @Override
    public void handle() {
        int type;
        try {
            while (true) {
                type = reader.next();

                if(type == AsyncXMLStreamReader.EVENT_INCOMPLETE) break;
                switch (type) {
                    case AsyncXMLStreamReader.START_ELEMENT:
                        if(within == 0) {
                            if (reader.getLocalName().equals("user")) {
                                within = 1;
                            } else if(reader.getLocalName().equals("password")) {
                                within = 2;
                            } else {
                                logger.warn("unknown xml element {}", reader.getLocalName());
                            }
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;
                    case AsyncXMLStreamReader.END_ELEMENT:
                        if(within == 0) {
                            if(reader.getLocalName().equals("auth")) {
                                // auth proceed...
                                if("wolong".equals(user) && "123".equals(password)) {
                                    logger.info("auth success");
                                    connection.pushState(new SessionState(connection, reader));
                                } else {
                                    logger.info("auth failed, user = {}, password = {}", user, password);
                                    connection.close();
                                }
                                //connection.setState(parent);

                                return;
                            } else {
                                logger.warn("unknown xml element {}", reader.getLocalName());
                            }
                        } else if(within == 1) {
                            if(reader.getLocalName().equals("user")) {
                                user = user_sb.toString();
                                within = 0;
                            } else {
                                logger.warn("unknown xml element {}", reader.getLocalName());
                            }
                        } else if(within == 2) {
                            if(reader.getLocalName().equals("password")) {
                                password = password_sb.toString();
                                within = 0;
                            } else {
                                logger.warn("unknown xml element {}", reader.getLocalName());
                            }
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;
                    case AsyncXMLStreamReader.CHARACTERS:
                        if(within == 1) {
                            user_sb.append(reader.getText());
                        } else if(within == 2) {
                            password_sb.append(reader.getText());
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML stream exception", e);
        }
    }
}
