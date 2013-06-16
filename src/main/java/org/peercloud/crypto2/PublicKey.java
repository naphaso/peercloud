package org.peercloud.crypto2;

import org.peercloud.crypto2.exception.EncryptionException;
import org.peercloud.crypto2.exception.LoadingException;
import org.peercloud.crypto2.exception.SaveException;
import org.peercloud.crypto2.exception.VerifyException;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/16/13
 * Time: 11:41 PM
 */
public abstract class PublicKey extends Key implements EncryptionKey, VerifyKey {

}
