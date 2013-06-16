package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.DecodeException;
import org.peercloud.crypto2.exception.EncodeException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 12:52 AM
 */
public abstract class Coder {
    public abstract String encode(byte[] data) throws EncodeException;
    public abstract byte[] decode(String data) throws DecodeException;
}
