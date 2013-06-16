package org.peercloud.crypto2.cipher.aes;

import org.peercloud.crypto2.SymmetricCipher;
import org.peercloud.crypto2.SymmetricKey;
import org.peercloud.crypto2.exception.GenerateException;
import org.peercloud.crypto2.exception.LoadingException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 2:15 AM
 */
public class CipherAES extends SymmetricCipher {
    public CipherAES() {
        this.symmetricKeyClass = CipherAESSimmetricKey.class;
    }

    @Override
    public SymmetricKey generateSymmetricKey() throws GenerateException {
        try {
            CipherAESSimmetricKey cipherAESSimmetricKey = new CipherAESSimmetricKey();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecretKey secretKey = keyGenerator.generateKey();
            cipherAESSimmetricKey.load(secretKey.getEncoded());
            return cipherAESSimmetricKey;
        } catch (NoSuchAlgorithmException e) {
            throw new GenerateException("no such algorithm", e);
        } catch (LoadingException e) {
            throw new GenerateException("loading exception", e);
        }
    }
}