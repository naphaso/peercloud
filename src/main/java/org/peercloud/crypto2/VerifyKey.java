package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.VerifyException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:44 PM
 */
public interface VerifyKey {
    public abstract void verify(byte[] data, byte[] signature) throws VerifyException;
}
