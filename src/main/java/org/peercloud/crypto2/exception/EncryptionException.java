package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 12:21 AM
 */
public class EncryptionException extends CipherException {
    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
