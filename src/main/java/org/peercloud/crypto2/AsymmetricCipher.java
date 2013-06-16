package org.peercloud.crypto2;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.peercloud.crypto2.exception.EncryptionException;
import org.peercloud.crypto2.exception.GenerateException;
import org.peercloud.crypto2.exception.LoadingException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:57 PM
 */
public abstract class AsymmetricCipher {
    public Class<? extends PublicKey> publicKeyClass;
    public Class<? extends PrivateKey> privateKeyClass;

    public PublicKey loadPublicKey(byte[] data) throws LoadingException {
        try {
            PublicKey publicKey = publicKeyClass.newInstance();
            publicKey.load(data);
            return publicKey;
        } catch (InstantiationException e) {
            throw  new LoadingException("invald public key class", e);
        } catch (IllegalAccessException e) {
            throw new LoadingException("can't load public key", e);
        }
    }

    public PrivateKey loadPrivateKey(byte[] data) throws LoadingException {
        try {
            PrivateKey privateKey = privateKeyClass.newInstance();
            privateKey.load(data);
            return privateKey;
        } catch (InstantiationException e) {
            throw new LoadingException("invalid private key class", e);
        } catch (IllegalAccessException e) {
            throw new LoadingException("can't load private key", e);
        }
    }

    public abstract AsymmetricKeyPair generateKeyPair() throws GenerateException;
}
