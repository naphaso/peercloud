package org.peercloud.network2.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/10/13
 * Time: 4:20 AM
 */
public class Identification implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Identification.class);

    public enum IdentificationMode { SERVER, CLIENT }
    private IdentificationMode mode;
    private XMLEventReader xmlstream;
    private boolean result;
    public Identification(XMLEventReader xmlstream, IdentificationMode mode) {
        this.xmlstream = xmlstream;
        this.mode = mode;
    }

    @Override
    public void run() {
        if(mode == IdentificationMode.SERVER) {
            try {
                XMLEvent event = xmlstream.nextEvent();
                if(event.isCharacters()) {
                    String data = event.asCharacters().getData();
                    logger.info("id data: '{}'", data);
                    result = true;

                } else {
                    result = false;
                }
            } catch (XMLStreamException e) {
                logger.warn("Identification XML exception", e);
                result = false;
            }
        }
    }
}
