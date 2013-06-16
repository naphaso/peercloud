package org.peercloud.crypto2.cipher.rsa;

import org.peercloud.crypto2.AsymmetricCipher;
import org.peercloud.crypto2.AsymmetricKeyPair;
import org.peercloud.crypto2.exception.GenerateException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 2:14 AM
 */
public class CipherRSA extends AsymmetricCipher {
    public CipherRSA() {
        this.privateKeyClass = CipherRSAPrivateKey.class;
        this.publicKeyClass = CipherRSAPublicKey.class;
    }

    @Override
    public AsymmetricKeyPair generateKeyPair() throws GenerateException {
        AsymmetricKeyPair asymmetricKeyPair = new AsymmetricKeyPair();
        CipherRSAPrivateKey cipherRSAPrivateKey = new CipherRSAPrivateKey();
        CipherRSAPublicKey cipherRSAPublicKey = new CipherRSAPublicKey();

        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new GenerateException("no such algorithm", e);
        }
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();

        cipherRSAPrivateKey.load(privateKey);
        cipherRSAPublicKey.load(publicKey);

        asymmetricKeyPair.privateKey = cipherRSAPrivateKey;
        asymmetricKeyPair.publicKey = cipherRSAPublicKey;

        return asymmetricKeyPair;
    }
}
