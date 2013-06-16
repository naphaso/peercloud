package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.EncryptionException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:43 PM
 */
public interface EncryptionKey {
    public abstract byte[] encrypt(byte[] data) throws EncryptionException;
}
