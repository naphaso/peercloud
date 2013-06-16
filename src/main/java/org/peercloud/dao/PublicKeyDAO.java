package org.peercloud.dao;

import org.peercloud.persistence.PublicKey;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/15/13
 * Time: 4:46 AM
 */
public interface PublicKeyDAO {
    public void add(PublicKey key);
    public void remove(PublicKey key);
    public List<PublicKey> list();


    /*
    public PublicKey byFingerprint(String fingerprint);
    */
}
