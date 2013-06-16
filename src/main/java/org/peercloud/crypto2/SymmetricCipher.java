package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.GenerateException;
import org.peercloud.crypto2.exception.LoadingException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:57 PM
 */
public abstract class SymmetricCipher {
    public Class<? extends SymmetricKey> symmetricKeyClass;

    public SymmetricKey loadSymmetricKey(byte[] data) throws LoadingException {
        try {
            SymmetricKey key = symmetricKeyClass.newInstance();
            key.load(data);
            return key;
        } catch (InstantiationException e) {
            throw new LoadingException("invalid symmetric key class", e);
        } catch (IllegalAccessException e) {
            throw new LoadingException("can't load symmetric key", e);
        }
    }

    public abstract SymmetricKey generateSymmetricKey() throws GenerateException;
}
