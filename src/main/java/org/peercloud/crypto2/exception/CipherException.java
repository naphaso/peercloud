package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 12:21 AM
 */
public class CipherException extends CryptoException {
    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }
}
