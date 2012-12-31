package org.peercloud.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/31/12
 * Time: 7:39 AM
 */
public class CertificateFactory {
    private static Logger logger = LoggerFactory.getLogger(CertificateFactory.class);
    private static CertificateFactory instance;

    private CertificateFactory() {
    }

    public static synchronized CertificateFactory getInstance() {
        if (instance == null)
            instance = new CertificateFactory();
        return instance;
    }

    public Certificate generate(String name) {

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();

            Certificate cert = new Certificate(name, privateKey, publicKey);
            System.out.println(cert.serialize());
            return cert;
        } catch (NoSuchAlgorithmException e) {
            logger.error("RSA not found", e);
            return null;
        }
    }
}
