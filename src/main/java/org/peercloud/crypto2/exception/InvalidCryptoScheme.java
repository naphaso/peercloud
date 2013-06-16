package org.peercloud.crypto2.exception;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 1:19 AM
 */
public class InvalidCryptoScheme extends CryptoException {
    public InvalidCryptoScheme(String message) {
        super(message);
    }

    public InvalidCryptoScheme(String message, Throwable cause) {
        super(message, cause);
    }
}
