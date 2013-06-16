package org.peercloud.persistence;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/10/13
 * Time: 4:30 AM
 */
@Entity
@Table
public class PublicKey {
    private static final Logger logger = LoggerFactory.getLogger(PublicKey.class);

    @Id
    private String fingerprint;

    @Column
    private String key;

    @OneToOne(optional = true)
    private PrivateKey privateKey;

    private RSAPublicKey publicKey;

    public PublicKey(String key) {
        this.key = key;
        generateFingerprint();
    }

    public PublicKey() {
    }

    private void generateFingerprint() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            digest.reset();
            //digest.update(publicKey.getModulus().toString(16).getBytes());
            //digest.update(":".getBytes());
            //digest.update(publicKey.getPublicExponent().toString(16).getBytes());
            digest.update(key.getBytes());

            fingerprint = Hex.encodeHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("generage fingerprint exception", e);
        }
    }

    private synchronized RSAPublicKey getPublicKey() {
        if(publicKey == null) {
            String[] publickeyParts = key.split(":");
            if(publickeyParts[0].equals("RSA")) {
                BigInteger modulo = new BigInteger(publickeyParts[1], 16);
                BigInteger publicExponent = new BigInteger(publickeyParts[2], 16);
                RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulo, publicExponent);
                try {
                    KeyFactory fact = KeyFactory.getInstance("RSA");
                    publicKey = (RSAPublicKey) fact.generatePublic(keySpec);
                } catch (NoSuchAlgorithmException e) {
                    logger.warn("error loading public key", e);
                } catch (InvalidKeySpecException e) {
                    logger.warn("error loading public key", e);
                }
            }
        }
        return publicKey;
    }

    private boolean checkFingerprint() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            digest.reset();
            //digest.update(publicKey.getModulus().toString(16).getBytes());
            //digest.update(":".getBytes());
            //digest.update(publicKey.getPublicExponent().toString(16).getBytes());
            digest.update(key.getBytes());

            String real_fingerprint = Hex.encodeHexString(digest.digest());
            return real_fingerprint.equals(fingerprint);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("error on checking fingerprint");
            return false;
        }
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
