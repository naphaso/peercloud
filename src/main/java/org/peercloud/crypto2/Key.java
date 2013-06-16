package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.LoadingException;
import org.peercloud.crypto2.exception.SaveException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 3:03 AM
 */
public abstract class Key {
    public abstract void load(byte[] data) throws LoadingException;
    public abstract byte[] save() throws SaveException;
}
