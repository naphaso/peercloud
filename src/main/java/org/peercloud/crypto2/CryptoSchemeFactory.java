package org.peercloud.crypto2;

import org.peercloud.crypto2.cipher.aes.CipherAES;
import org.peercloud.crypto2.cipher.base64.CipherBase64;
import org.peercloud.crypto2.cipher.rsa.CipherRSA;
import org.peercloud.crypto2.exception.InvalidCryptoScheme;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/17/13
 * Time: 1:17 AM
 */
public class CryptoSchemeFactory {
    protected Map<String, Class<? extends AsymmetricCipher>> asymmetricCiphers = new HashMap<>();
    protected Map<String, Class<? extends SymmetricCipher>> symmetricCiphers = new HashMap<>();
    protected Map<String, Class<? extends Coder>> coders = new HashMap<>();

    public CryptoSchemeFactory() {
        asymmetricCiphers.put("RSA", CipherRSA.class);
        symmetricCiphers.put("AES", CipherAES.class);
        coders.put("BASE64", CipherBase64.class);
    }

    public CryptoScheme getCryptoScheme(String name) throws InvalidCryptoScheme{
        String[] parts = name.split(":");
        if(parts.length != 3)
            throw new InvalidCryptoScheme("invalid count of parts");


        Class<? extends AsymmetricCipher> asymmetricCipherClass = asymmetricCiphers.get(parts[0]);
        if(asymmetricCipherClass == null) {
            throw new InvalidCryptoScheme("invalid asymmetric cipher name " + parts[0]);
        }
        Class<? extends SymmetricCipher> symmetricCipherClass = symmetricCiphers.get(parts[1]);
        if(symmetricCipherClass == null) {
            throw new InvalidCryptoScheme("invalid symmetric cipher name " + parts[1]);
        }
        Class<? extends Coder> coderClass = coders.get(parts[2]);
        if(coderClass == null) {
            throw new InvalidCryptoScheme("invalid coder name " + parts[2]);
        }

        try {
            AsymmetricCipher asymmetricCipher = asymmetricCipherClass.newInstance();
            SymmetricCipher symmetricCipher = symmetricCipherClass.newInstance();
            Coder coder = coderClass.newInstance();

            return new CryptoScheme(asymmetricCipher, symmetricCipher, coder);
        } catch (InstantiationException e) {
            throw new InvalidCryptoScheme("can't instantiate cipher", e);
        } catch (IllegalAccessException e) {
            throw new InvalidCryptoScheme("can't instantiate cipher", e);
        }
    }

    public static void main(String[] args) throws Exception {
        CryptoSchemeFactory cryptoSchemeFactory = new CryptoSchemeFactory();
        CryptoScheme cryptoScheme = cryptoSchemeFactory.getCryptoScheme("RSA:AES:BASE64");
        AsymmetricKeyPair asymmetricKeyPair = cryptoScheme.asymmetricCipher.generateKeyPair();

        String data = cryptoScheme.encryptToString(asymmetricKeyPair.publicKey, "hello world :D");
        String decoded = cryptoScheme.decryptToString(asymmetricKeyPair.privateKey, data);

        SymmetricKey symmetricKey = cryptoScheme.symmetricCipher.generateSymmetricKey();

        String e = cryptoScheme.encryptToString(symmetricKey, "hi there ;)");
        System.out.println(e);
        String p = cryptoScheme.decryptToString(symmetricKey, e);
        System.out.println(p);
        //System.out.println(cryptoScheme.saveKeyToString(asymmetricKeyPair.publicKey));
        //System.out.println(cryptoScheme.saveKeyToString(asymmetricKeyPair.privateKey));
    }
}
