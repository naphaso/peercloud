package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 12:22 AM
 */
public class DecryptionException extends CipherException {
    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
