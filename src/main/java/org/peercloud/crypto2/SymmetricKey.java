package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.GenerateException;
import org.peercloud.crypto2.exception.LoadingException;
import org.peercloud.crypto2.exception.SaveException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:42 PM
 */
public abstract class SymmetricKey extends Key implements EncryptionKey, DecryptionKey, SignKey, VerifyKey {
}
