package org.peercloud;

import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 9:18 AM
 */
public class F2FConnection {
    private Logger logger = LoggerFactory.getLogger(F2FConnection.class);
    private static final InputFactoryImpl readerFactory = new InputFactoryImpl();
    private AsyncXMLStreamReader reader;
    private AsyncInputFeeder inputFeeder;

    private Channel channel;

    public F2FConnection(Channel channel) {
        this.channel = channel;

        reader = readerFactory.createAsyncXMLStreamReader();
        inputFeeder = reader.getInputFeeder();
    }

    public void close() {
        channel.close();
    }

    public void handleData(ByteBuf in) {
        logger.debug("handle data for connection id {}, data = '{}'", channel.id(), in.toString(0, in.readableBytes(), Charset.forName("UTF-8")));

        try {
            inputFeeder.feedInput(in.array(), 0, in.readableBytes());
            in.clear();
        } catch (XMLStreamException e) {
            logger.error("XML stream feed exception", e);
        }

        int type;
        try {
            while(true) {
                type = reader.next();
                *   if(type == AsyncXMLStreamReader.EVENT_INCOMPLETE)
                    break;

                switch(state) {
                    case INIT:
                        if(type == AsyncXMLStreamReader.START_DOCUMENT)
                            state = State.STREAM_UNAUTH;
                        break;
                    case STREAM_UNAUTH:
                        if(type == AsyncXMLStreamReader.START_ELEMENT)
                            if(reader.getLocalName().equals("auth")) {

                            }
                    case STREAM_AUTH:
                }
                if (type == AsyncXMLStreamReader.START_ELEMENT) {
                    logger.debug("start element {}", reader.getLocalName());
                }

                if(type == AsyncXMLStreamReader.END_DOCUMENT) {

                }
                logger.debug("type = {}", type);
            }
        } catch (XMLStreamException e) {
            logger.error("XML stream exception: {}", e);
        }
    }

    public void handleUnregister() {
        // if authenticated, notify FriendManager
    }
}
