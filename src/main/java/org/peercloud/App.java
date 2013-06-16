package org.peercloud;

import com.thoughtworks.xstream.XStream;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector;
import org.hsqldb.rowio.RowOutputText;
import org.peercloud.crypto.Certificate;
import org.peercloud.crypto.CertificateFactory;
import org.peercloud.crypto.CertificateStorage;
import org.peercloud.dao.NoteDAO;
import org.peercloud.network.Server;
import org.peercloud.network.data.LinkID;
import org.peercloud.network.data.RouteAction;
import org.peercloud.network.data.RoutingTable;
import org.peercloud.persistence.Note;
import org.peercloud.service.NoteService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Timer;

public class App {
    private static AbstractApplicationContext context;
    public AbstractApplicationContext getContext() {
        return context;
    }
    public static void main(String[] args) {


        context = new ClassPathXmlApplicationContext("contexts/server.xml");



        //RoutingTable routingTable = (RoutingTable) context.getBean("routingTable");
        //routingTable.addRoute(new LinkID("30313233343536373839"), new RouteAction(new InetSocketAddress("127.0.0.1", 9092)));



        /*
        NoteService noteService = context.getBean(NoteService.class);

        for(int i = 0; i < 10000; i++) {
            Note note = new Note();
            note.setContent("asdasd " + i);
            note.setId(1000+i);
            noteService.addNote(note);

        }

        List<Note> notes = noteService.listNote();
        for(Note note1 : notes) {
            System.out.println("NOTE CONTENT::::::::::::::::::::: " + note1.getContent());
        }

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        //Server.getInstance().run();

        //server.run();

        /*
        Certificate cert = new Certificate(
                "name: alice.users.peercloud\n" +
                "publickey: 87fca17fe07655be7e56475ad98eb00950545f592a3e12b7b6975aeb279ebae007bb135b58161b0fc3e44c41ecc180b566d63c90bed37982db656604455c1f00158d3f3a8da034fbad5b3b1a20f1b662ad9aa448c3c804f51ce47c8ba1000ab6923e1187f10a730ca8c00cddf3d712a4cbd1360c033df8dac87156a46e583aa30d8fe0c955431c369cb42d8746d14b97e4ecf0168abab0c0da65eb191bb5788cda8fccd5242c7b233ac5538757748b261b713383d713ab6aa7a31bffab9222427c8cb612a319486d8c3211c8100f20d41c9b8b4940ad266d43fee439550e518f732f59ec9e2e48a916b103dd8eb0a97f292c0303af2bc63190fd521bd91921ab:10001\n" +
                "sign: eve[21.12.2012-21.12.2013.12:21:12]d1bc8d3ba4afc7e109612cb73acbdddac052c93025aa1f82942edabb7deb82a1\n" +
                "sign: bob[-]56b4e3325668ddef338f776f1698de014694a3f5de91b4d806c43e97d9529dd7\n");


        xstream.processAnnotations(Certificate.class);
        String xml = xstream.toXML(cert);
        System.out.println(xml);*/
        /*
        CertificateStorage storage = CertificateStorage.getInstance();
        Certificate alice = storage.getCertificate("alice.users.peercloud");
        XStream xstream = new XStream();
        String xml = xstream.toXML(alice);
        System.out.println(xml);*/
        //Certificate cert = CertificateFactory.getInstance().generate("wolong");
        //CertificateStorage.getInstance().saveCertificate(cert);
        //CertificateStorage storage = CertificateStorage.getInstance();

        //Certificate certificate = CertificateStorage.getInstance()
        //        .getCertificateByFingerprint("ccd92c8d94aedf0f9fd150c5b5b5e0e6e3ea09bb");
        //certificate.checkSigns();
    }
}
