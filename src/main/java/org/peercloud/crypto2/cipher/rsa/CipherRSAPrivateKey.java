package org.peercloud.crypto2.cipher.rsa;

import org.peercloud.crypto2.PrivateKey;
import org.peercloud.crypto2.exception.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 2:07 AM
 */
public class CipherRSAPrivateKey extends PrivateKey {
    protected RSAPrivateKey rsaPrivateKey;

    @Override
    public void load(byte[] data) throws LoadingException {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            short moduloSize = byteBuffer.getShort();
            byte[] moduloBytes = new byte[moduloSize];
            byteBuffer.get(moduloBytes);
            short exponentSize = byteBuffer.getShort();
            byte[] exponentBytes = new byte[exponentSize];
            byteBuffer.get(exponentBytes);

            BigInteger modulo = new BigInteger(moduloBytes);
            BigInteger privateExponent = new BigInteger(exponentBytes);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulo, privateExponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            rsaPrivateKey = (RSAPrivateKey) fact.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new LoadingException("can't load private key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new LoadingException("can't load private key", e);
        } catch (BufferUnderflowException e) {
            throw new LoadingException("invalid private key", e);
        }
    }

    public void load(RSAPrivateKey rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    @Override
    public byte[] save() throws SaveException {
        byte[] exponent = rsaPrivateKey.getPrivateExponent().toByteArray();
        byte[] modulo = rsaPrivateKey.getModulus().toByteArray();

        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(exponent.length + modulo.length + 4);
            byteBuffer.putShort((short)modulo.length);
            byteBuffer.put(modulo);
            byteBuffer.putShort((short)exponent.length);
            byteBuffer.put(exponent);

            return byteBuffer.array();
        } catch (BufferOverflowException e) {
            throw new SaveException("save private key exception", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data) throws DecryptionException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            cipher.update(data);
            return cipher.doFinal();
        } catch (IllegalBlockSizeException e) {
            throw new DecryptionException("illegal block size", e);
        } catch (InvalidKeyException e) {
            throw new DecryptionException("invlid key", e);
        } catch (BadPaddingException e) {
            throw new DecryptionException("bad padding", e);
        } catch (NoSuchAlgorithmException e) {
            throw new DecryptionException("RSA not found", e);
        } catch (NoSuchPaddingException e) {
            throw new DecryptionException("invalid padding", e);
        }
    }

    @Override
    public byte[] sign(byte[] data) throws SignException {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(rsaPrivateKey);
            sign.update(data);
            return sign.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new SignException("no such algorithm", e);
        } catch (InvalidKeyException e) {
            throw new SignException("invalid key", e);
        } catch (SignatureException e) {
            throw new SignException("verify exception", e);
        }
    }
}
