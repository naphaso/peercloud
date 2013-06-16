package org.peercloud.crypto2.cipher.rsa;

import org.peercloud.crypto2.PublicKey;
import org.peercloud.crypto2.exception.EncryptionException;
import org.peercloud.crypto2.exception.LoadingException;
import org.peercloud.crypto2.exception.SaveException;
import org.peercloud.crypto2.exception.VerifyException;

import javax.crypto.*;
import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 1:33 AM
 */
public class CipherRSAPublicKey extends PublicKey {
    private RSAPublicKey rsaPublicKey;

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
            BigInteger publicExponent = new BigInteger(exponentBytes);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulo, publicExponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            rsaPublicKey = (RSAPublicKey) fact.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new LoadingException("can't load public key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new LoadingException("can't load public key", e);
        } catch (BufferUnderflowException e) {
            throw new LoadingException("invalid public key", e);
        }
    }

    public void load(RSAPublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    @Override
    public byte[] save() throws SaveException {
        byte[] exponent = rsaPublicKey.getPublicExponent().toByteArray();
        byte[] modulo = rsaPublicKey.getModulus().toByteArray();

        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(exponent.length + modulo.length + 4);
            byteBuffer.putShort((short)modulo.length);
            byteBuffer.put(modulo);
            byteBuffer.putShort((short)exponent.length);
            byteBuffer.put(exponent);

            return byteBuffer.array();
        } catch (BufferOverflowException e) {
            throw new SaveException("save public key exception", e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            cipher.update(data);
            return cipher.doFinal();
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException("illegal block size", e);
        } catch (InvalidKeyException e) {
            throw new EncryptionException("invlid key", e);
        } catch (BadPaddingException e) {
            throw new EncryptionException("bad padding", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("RSA not found", e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException("invalid padding", e);
        }
    }

    @Override
    public void verify(byte[] data, byte[] signature) throws VerifyException {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(rsaPublicKey);
            sign.update(data);
            if(!sign.verify(signature)) {
                throw new VerifyException("invalid signature");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new VerifyException("no such algorithm", e);
        } catch (InvalidKeyException e) {
            throw new VerifyException("invalid key", e);
        } catch (SignatureException e) {
            throw new VerifyException("verify exception", e);
        }
    }
}
