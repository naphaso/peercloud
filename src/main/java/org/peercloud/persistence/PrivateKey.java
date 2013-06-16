package org.peercloud.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.security.interfaces.RSAPrivateKey;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/15/13
 * Time: 4:22 AM
 */
@Entity
@Table
public class PrivateKey {
    private static final Logger logger = LoggerFactory.getLogger(PrivateKey.class);

    @Id
    private String fingerprint;

    @Column
    private String key;

    @OneToOne(optional = false)
    private PublicKey publicKey;

    private RSAPrivateKey privateKey;


    public PrivateKey() {
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
