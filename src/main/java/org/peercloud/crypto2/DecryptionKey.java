package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.DecryptionException;
import org.peercloud.crypto2.exception.SignException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:43 PM
 */
public interface DecryptionKey {
    public abstract byte[] decrypt(byte[] data) throws DecryptionException;
}
