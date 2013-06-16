package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.SignException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:44 PM
 */
public interface SignKey {
    public abstract byte[] sign(byte[] data) throws SignException;
}
