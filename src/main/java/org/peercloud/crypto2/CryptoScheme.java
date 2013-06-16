package org.peercloud.crypto2;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.peercloud.crypto2.exception.*;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:40 PM
 */
public class CryptoScheme {
    protected AsymmetricCipher asymmetricCipher;
    protected SymmetricCipher symmetricCipher;
    protected Coder coder;

    public CryptoScheme(AsymmetricCipher asymmetricCipher, SymmetricCipher symmetricCipher, Coder coder) {
        this.asymmetricCipher = asymmetricCipher;
        this.symmetricCipher = symmetricCipher;
        this.coder = coder;
    }

    public byte[] saveKeyToData(Key key) throws SaveException {
        return key.save();
    }

    public String saveKeyToString(Key key) throws SaveException, EncodeException {
        return coder.encode(key.save());
    }

    public PublicKey loadPublicKey(String data) throws DecodeException, LoadingException {
        return asymmetricCipher.loadPublicKey(coder.decode(data));
    }

    public PrivateKey loadPrivateKey(String data) throws DecodeException, LoadingException {
        return asymmetricCipher.loadPrivateKey(coder.decode(data));
    }

    public SymmetricKey loadSymmetricKey(String data) throws DecodeException, LoadingException {
        return symmetricCipher.loadSymmetricKey(coder.decode(data));
    }

    // encryption

    public byte[] encryptToData(EncryptionKey key, byte[] data) throws EncryptionException {
        return key.encrypt(data);
    }

    public byte[] encryptToData(EncryptionKey key, String data) throws EncryptionException {
        return encryptToData(key, data.getBytes());
    }

    public String encryptToString(EncryptionKey key, byte[] data) throws EncryptionException, EncodeException {
        return coder.encode(encryptToData(key, data));
    }

    public String encryptToString(EncryptionKey key, String data) throws EncryptionException, EncodeException {
        return encryptToString(key, data.getBytes());
    }

    // decryption

    public byte[] decryptToData(DecryptionKey key, byte[] data) throws DecryptionException {
        return key.decrypt(data);
    }

    public byte[] decryptToData(DecryptionKey key, String data) throws DecodeException, DecryptionException {
        return key.decrypt(coder.decode(data));
    }

    public String decryptToString(DecryptionKey key, byte[] data) throws DecryptionException {
        return new String(decryptToData(key, data));
    }

    public String decryptToString(DecryptionKey key, String data) throws DecodeException, DecryptionException {
        return decryptToString(key, coder.decode(data));
    }

    // sign

    public byte[] signToData(SignKey key, byte[] data) throws SignException {
        return key.sign(data);
    }

    public byte[] signToData(SignKey key, String data) throws SignException {
        return signToData(key, data.getBytes());
    }

    public String signToString(SignKey key, byte[] data) throws SignException, EncodeException {
        return coder.encode(signToData(key, data));
    }

    public String signToString(SignKey key, String data) throws EncodeException, SignException {
        return signToString(key, data.getBytes());
    }

    // verify

    public void verify(VerifyKey key, byte[] data, byte[] signature) throws VerifyException {
        key.verify(data, signature);
    }

    public void verify(VerifyKey key, String data, String signature) throws VerifyException, DecodeException {
        key.verify(coder.decode(data), coder.decode(signature));
    }
}
