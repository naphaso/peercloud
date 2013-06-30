package org.peercloud.xml;

import com.fasterxml.aalto.AsyncXMLStreamReader;
import io.netty.channel.ChannelFuture;
import org.peercloud.Friend;
import org.peercloud.FriendManager;
import org.peercloud.crypto.Certificate;
import org.peercloud.network2.F2FConnection;
import org.peercloud.network2.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.util.concurrent.TimeUnit;

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

        if (connection.isClient())
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
                if (type == AsyncXMLStreamReader.EVENT_INCOMPLETE) break;
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
                        if (elementName.equals("id")) {
                            if (in_id) {
                                String certString = certBuilder.toString();
                                Certificate certificate = new Certificate(certString);
                                Friend friend;
                                if (certificate.isValid()) {
                                    friend = FriendManager.getInstance().getFriend(certificate.getFingerprint());
                                    if (friend == null) {
                                        // you are not my friend
                                        logger.debug("you are not my friend");
                                        ChannelFuture future = connection.getChannel().write("<fatal type=\"id\">you are not my friend</fatal>");
                                        try {
                                            future.await(5, TimeUnit.SECONDS);
                                        } catch (InterruptedException e) {
                                            logger.error("Interrupted", e);
                                        } finally {
                                            connection.close();
                                            return;
                                        }
                                    } else {
                                        logger.debug("valid friend identification");
                                    }
                                } else {
                                    // invalid certificate
                                    logger.debug("invalid certificate");
                                    ChannelFuture future = connection.getChannel().write("<fatal type=\"id\">invalid certificate</fatal>");
                                    try {
                                        future.await(5, TimeUnit.SECONDS);
                                    } catch (InterruptedException e) {
                                        logger.error("Interrupted", e);
                                    } finally {
                                        connection.close();
                                        return;
                                    }
                                }

                                if (!connection.isClient()) {
                                    connection.getChannel().write("<id>" +
                                            Server.getInstance().getServerCertificate().serialize(false) +
                                            "</id>\n");

                                }

                                AuthState state = new AuthState(connection, reader);
                                connection.pushState(state);
                                state.handle();
                            }
                        } else if (elementName.equals("stream")) {
                            logger.debug("stream ended, F2F connection closed");
                            connection.close();
                        } else {
                            logger.warn("unknown xml element {}", reader.getLocalName());
                        }
                        break;
                    case AsyncXMLStreamReader.CHARACTERS:
                        if (in_id)
                            certBuilder.append(reader.getText());
                        break;

                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML stream exception", e);
            connection.close();
        }
    }
}
