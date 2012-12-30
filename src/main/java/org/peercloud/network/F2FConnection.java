package org.peercloud.network;

import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.peercloud.xml.InitState;
import org.peercloud.xml.State;
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

    private State state;

    public F2FConnection(Channel channel) {
        this.channel = channel;

        reader = readerFactory.createAsyncXMLStreamReader();
        inputFeeder = reader.getInputFeeder();

        state = new InitState(this, reader);
    }

    public void setState(State state) {
        this.state = state;
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

        state.handle();
    }

    public void handleUnregister() {
        // if authenticated, notify FriendManager
    }


}
