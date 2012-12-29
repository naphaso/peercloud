package org.peercloud;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class App {
    public static void main(String[] args) {
        //System.out.println("Hello World!");
        //Server server = new Server();
        //server.run();
        /*
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            Key publicKey = kp.getPublic();
            Key privateKey = kp.getPrivate();
            //publicKey.getFormat();

            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
                    RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
                    RSAPrivateKeySpec.class);
            System.out.println("modulo = " + pub.getModulus() + ", public exponent = " + pub.getPublicExponent());
            System.out.println("modulo = " + priv.getModulus() + ", private exponent = " + priv.getPrivateExponent());
            //System.out.println("publickey = " + new String(publicKey.getEncoded()));
            //System.out.println("privatekey = " + new String(privateKey.getEncoded()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        */
        Certificate cert = new Certificate("name: alice.users.peercloud\npublickey: 123\nsign: bob.[-]321\nauth: somedata\n");
    }
}
