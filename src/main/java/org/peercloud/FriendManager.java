package org.peercloud;

import com.thoughtworks.xstream.XStream;
import org.peercloud.network.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 9:16 AM
 */
public class FriendManager {
    private static Logger logger = LoggerFactory.getLogger(Friend.class);
    private static FriendManager instance;
    List<Friend> friends;

    private FriendManager() {
        XStream xstream = new XStream();
        xstream.processAnnotations(Friend.class);
        friends = (List<Friend>) xstream.fromXML(new File(Server.getInstance().getConfig().getBasedir() + "/friends.xml"));
    }

    public static synchronized FriendManager getInstance() {
        if (instance == null)
            instance = new FriendManager();
        return instance;
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    public void save() {
        XStream xstream = new XStream();
        xstream.processAnnotations(Friend.class);
        try {
            FileWriter fileWriter = new FileWriter("friends.xml");
            xstream.toXML(friends, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            logger.error("error write friends.xml", e);
        }
    }

    public Friend getFriend(String fingerprint) {
        for(Friend friend : friends)
            if(friend.getFingerprint().equals(fingerprint))
                return friend;
        return null;
    }

    public boolean inNetwork() {
        for(Friend friend : friends)
            if(friend.getStatus() == Friend.Status.ONLINE)
                return true;
        return false;
    }
}
