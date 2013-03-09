package org.peercloud.network.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/1/13
 * Time: 1:22 AM
 */
public class ControlHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ControlHandler.class);
    private static XMLInputFactory inputFactory = XMLInputFactory.newInstance();

    private Socket clientSocket;

    public ControlHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        logger.debug("new client handler");
        try {
            InputStream inputStream = clientSocket.getInputStream();
            XMLEventReader xmlstream = inputFactory.createXMLEventReader(inputStream);

            XMLEvent event = xmlstream.nextTag();
            if(!event.isStartElement() || !event.asStartElement().getName().getLocalPart().equals("control"))
                return;

            // TODO: identification
            event = xmlstream.nextTag();
            if(!event.isStartElement() || !event.asStartElement().getName().getLocalPart().equals("id"))
                return;
            Identification identification = new Identification(xmlstream, Identification.IdentificationMode.SERVER);
            identification.run();


            // TODO: authentification

            // TODO: authorization

            // xml stream parse
            while(xmlstream.hasNext()) {
                event = xmlstream.nextTag();

                //logger.info("event = {}", event);
            }
        } catch (IOException e) {
            logger.error("client error", e);
        } catch (XMLStreamException e) {
            logger.error("XML Stream error", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.warn("error in closing socket", e);
            }
            logger.debug("client handler terminated");
        }
    }
}
