package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 2:48 AM
 */
public class GenerateException extends CipherException {
    public GenerateException(String message) {
        super(message);
    }

    public GenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}
