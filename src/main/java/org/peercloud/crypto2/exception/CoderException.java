package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 12:55 AM
 */
public class CoderException extends CipherException {
    public CoderException(String message) {
        super(message);
    }

    public CoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
