package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 12:22 AM
 */
public class VerifyException extends CipherException {
    public VerifyException(String message) {
        super(message);
    }

    public VerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
