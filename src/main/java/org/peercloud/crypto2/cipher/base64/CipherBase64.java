package org.peercloud.crypto2.cipher.base64;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.peercloud.crypto2.Coder;
import org.peercloud.crypto2.exception.DecodeException;
import org.peercloud.crypto2.exception.EncodeException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 2:44 AM
 */
public class CipherBase64 extends Coder {
    @Override
    public String encode(byte[] data) throws EncodeException {
        // TODO: exception
        return Base64.encode(data);
    }

    @Override
    public byte[] decode(String data) throws DecodeException {
        // TODO: exception
        return Base64.decode(data);
    }
}
