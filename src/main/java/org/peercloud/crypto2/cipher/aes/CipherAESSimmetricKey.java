package org.peercloud.crypto2.cipher.aes;

import org.peercloud.crypto2.SymmetricKey;
import org.peercloud.crypto2.exception.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 2:13 AM
 */
public class CipherAESSimmetricKey extends SymmetricKey {
    protected SecretKey secretKeyCrypt;
    protected SecretKey secretKeySign;

    @Override
    public void load(byte[] data) throws LoadingException {
        secretKeyCrypt = new SecretKeySpec(data, "AES");
        secretKeySign = new SecretKeySpec(data, "HmacSHA256");
    }


    @Override
    public byte[] save() throws SaveException {
        return secretKeyCrypt.getEncoded();
    }

    @Override
    public byte[] decrypt(byte[] data) throws DecryptionException {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            int ivSize = byteBuffer.getInt();
            byte[] iv = new byte[ivSize];
            byteBuffer.get(iv);
            int ciphertextSize = byteBuffer.getInt();
            byte[] ciphertext = new byte[ciphertextSize];
            byteBuffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeyCrypt, new IvParameterSpec(iv));
            return cipher.doFinal(ciphertext);
        } catch (IllegalBlockSizeException e) {
            throw new DecryptionException("illegal block size", e);
        } catch (InvalidKeyException e) {
            throw new DecryptionException("invalid key", e);
        } catch (BadPaddingException e) {
            throw new DecryptionException("bad padding", e);
        } catch (NoSuchAlgorithmException e) {
            throw new DecryptionException("not such algorithm", e);
        } catch (NoSuchPaddingException e) {
            throw new DecryptionException("no such padding", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new DecryptionException("invalid algorithm parameter", e);
        }

    }

    @Override
    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyCrypt);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ciphertext = cipher.doFinal(data);
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length + 8);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.putInt(ciphertext.length);
            byteBuffer.put(ciphertext);
            return byteBuffer.array();
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException("illegal block size", e);
        } catch (InvalidKeyException e) {
            throw new EncryptionException("invalid key", e);
        } catch (BadPaddingException e) {
            throw new EncryptionException("bad padding", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("no such algorithm", e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException("no such padding", e);
        } catch (InvalidParameterSpecException e) {
            throw new EncryptionException("invalid parameter spec", e);
        }
    }

    @Override
    public byte[] sign(byte[] data) throws SignException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySign);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new SignException("no such algorithm", e);
        } catch (InvalidKeyException e) {
            throw new SignException("invalid key", e);
        }
    }

    @Override
    public void verify(byte[] data, byte[] signature) throws VerifyException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySign);
            byte[] signature2 = mac.doFinal(data);
            if(!Arrays.equals(signature2, signature)) {
                throw new VerifyException("invalid signature");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new VerifyException("no such algorithm", e);
        } catch (InvalidKeyException e) {
            throw new VerifyException("invalid key", e);
        }
    }
}