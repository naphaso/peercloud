package org.peercloud.crypto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/30/12
 * Time: 4:35 AM
 */

public class Certificate {
    static Logger logger = LoggerFactory.getLogger(Certificate.class);
    static Pattern intervalPattern = Pattern.compile("^([a-zA-Z0-9]+)\\[([0-9:.]*)-([0-9:.]*)\\]([0-9a-f]+)$");
    static DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy.HH:mm:ss");
    static DateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy");

    static {
        dateFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    String name;
    String fingerprint;
    RSAPublicKey publicKey;
    RSAPrivateKey privateKey;
    SortedMap<String, String> fields = new TreeMap<>();
    List<Sign> signs = new ArrayList<>();
    boolean valid = false;

    public Certificate(File file) throws IOException {
        this(FileUtils.readFileToString(file));
    }

    public Certificate(String name, RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
        this.name = name;
        this.privateKey = rsaPrivateKey;
        this.publicKey = rsaPublicKey;
        try {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(publicKey.getModulus().toString(16).getBytes());
        digest.update(":".getBytes());
        digest.update(publicKey.getPublicExponent().toString(16).getBytes());
        this.fingerprint = Hex.encodeHexString(digest.digest());
        } catch(NoSuchAlgorithmException e) {
            logger.error("SHA-1 not found", e);
            return;
        }
        valid = true;
    }

    public Certificate(String source) {
        logger.debug("loading certificate '{}'", source);

        StringReader reader = new StringReader(source);
        BufferedReader bufferedReader = new BufferedReader(reader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(":\\s*", 2);
                if (tokens.length == 2) {
                    if (tokens[0].equals("sign"))
                        signs.add(new Sign(tokens[1]));
                    else if(tokens[0].equals("fingerprint"))
                        fingerprint = tokens[1];
                    else
                        fields.put(tokens[0], tokens[1]);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            logger.error("exception", e);
            return;
        }

        try {
            String[] publickeyParts = fields.get("publickey").split(":");
            BigInteger modulo = new BigInteger(publickeyParts[0], 16);
            BigInteger publicExponent = new BigInteger(publickeyParts[1], 16);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulo, publicExponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey) fact.generatePublic(keySpec);

            if(fields.containsKey("privatekey")) {
                String[] privatekeyParts = fields.get("privatekey").split(":");
                BigInteger modulus = new BigInteger(privatekeyParts[0], 16);
                BigInteger privateExponent = new BigInteger(privatekeyParts[1], 16);
                RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
                privateKey = (RSAPrivateKey) fact.generatePrivate(privateKeySpec);
            }

        } catch (NoSuchAlgorithmException e) {
            logger.error("RSA not found?", e);
            return;
        } catch (InvalidKeySpecException e) {
            logger.error("invalid public key", e);
            return;
        }

        name = fields.get("name");
        if(!name.matches("^[a-zA-Z0-9.]+$")) {
            logger.error("invalid name in certificate: {}", name);
            return;
        }


        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(publicKey.getModulus().toString(16).getBytes());
            digest.update(":".getBytes());
            digest.update(publicKey.getPublicExponent().toString(16).getBytes());

            String real_fingerprint = Hex.encodeHexString(digest.digest());
            if(fingerprint != null && !real_fingerprint.equals(fingerprint)) {
                logger.error("invalid fingerprint in {} certificate: {} != {}", name, fingerprint, real_fingerprint);
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA-1 not found");
            return;
        }
        valid = true; // all correct
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getSignedString() {
        StringBuilder stringBuilder = new StringBuilder();
        /*
        for (SortedMap.Entry<String, String> e : fields.entrySet()) {
            stringBuilder.append(e.getKey());
            stringBuilder.append(':');
            stringBuilder.append(e.getValue());
            stringBuilder.append('\n');
        }*/
        stringBuilder.append("name:").append(name).append('\n');
        stringBuilder.append("publickey")
                .append(publicKey.getModulus().toString(16)).append(":")
                .append(publicKey.getPublicExponent().toString(16)).append("\n");
        return stringBuilder.toString();
    }

    public void addSignature(Sign sign) {
        signs.add(sign);
        CertificateStorage.getInstance().updateCertificate(this);
    }

    public Sign signCertificate(Certificate cert, String from, String to) {
        if(privateKey == null) {
            logger.error("signing by public certificate");
            return null;
        }
        try {
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);
            String fromto = from + "-" + to;
            signer.update((cert.getSignedString() + fromto).getBytes());
            String signStr = Hex.encodeHexString(signer.sign());
            Sign sign = new Sign(this.getFingerprint() + "[" + fromto + "]" + signStr);
            cert.addSignature(sign);

            return sign;
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA256withRSA not found", e);
        } catch (InvalidKeyException e) {
            logger.error("invalid key", e);
        } catch (SignatureException e) {
            logger.error("signature exception", e);
        }
        return null;
    }

    public boolean checkSignature(Certificate cert, Sign signature) {
        if (!signature.checkValidity()) return false;
        try {
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(this.getPublicKey());
            verifier.update((cert.getSignedString() + signature.getFromto()).getBytes());
            return verifier.verify(signature.getSignature());
        } catch (InvalidKeyException e) {
            logger.error("invalid public key", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("RSA not found", e);
        } catch (SignatureException e) {
            logger.error("signature check exception", e);
        }
        return false;
    }

    public String serialize(boolean withPrivateKey) {
        if(valid) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name: ").append(name).append("\n");
            stringBuilder.append("fingerprint: ").append(fingerprint).append("\n");
            stringBuilder.append("publickey: ")
                    .append(publicKey.getModulus().toString(16)).append(":")
                    .append(publicKey.getPublicExponent().toString(16)).append("\n");
            if(withPrivateKey && privateKey != null)
                stringBuilder.append("privatekey: ")
                        .append(privateKey.getModulus().toString(16)).append(":")
                        .append(privateKey.getPrivateExponent().toString(16)).append("\n");
            for(Sign sign : signs) {
                //sign.checkValidity();
                //if(sign.isValid())
                    stringBuilder.append("sign: ").append(sign.serialize()).append("\n");

            }
            return stringBuilder.toString();
        }

        return "";
    }

    public boolean isValid() {
        return valid;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void checkSigns() {
        logger.debug("checking signatures for cert {}", fingerprint);
        for(Sign sign : signs) {
            logger.debug("checking signature from {}", sign.getAuthor());
            Certificate authorCert = CertificateStorage.getInstance().getCertificateByFingerprint(sign.getAuthor());
            if(authorCert == null) {
                logger.debug("unknown author");
            } else {
                if(authorCert.checkSignature(this, sign)) {
                    logger.debug("signature from {} valid", sign.getAuthor());
                } else {
                    logger.debug("signature from {} invalid", sign.getAuthor());
                }
            }
        }
    }

    public class Sign {
        boolean valid;
        String source;
        String fromto;
        String author;
        byte[] signature;
        long from;
        long to;

        public Sign(String source) {
            this.source = source;
            Matcher matcher = intervalPattern.matcher(source);
            if (matcher.find()) {
                try {
                    author = matcher.group(1);
                    from = dateToTimestamp(matcher.group(2));
                    to = dateToTimestamp(matcher.group(3));
                    fromto = matcher.group(2) + "-" + matcher.group(3);
                    signature = Hex.decodeHex(matcher.group(4).toCharArray());
                } catch (DecoderException e) {
                    logger.error("invalid signature hex string");
                }
            }
        }

        public String serialize() {
            return source;
        }

        public String getFromto() {
            return fromto;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isValid() {
            return valid;
        }

        public byte[] getSignature() {
            return signature;
        }

        public boolean checkValidity() {
            long time = new Date().getTime();
            return (from == -1 || from < time) && (to == -1 || to > time);
        }

        private long dateToTimestamp(String date) {
            if (date.isEmpty()) return -1;
            try {
                return dateFormat1.parse(date).getTime();
            } catch (ParseException e) {
                try {
                    return dateFormat2.parse(date).getTime();
                } catch (ParseException e1) {
                    return -1;
                }
            }
        }
    }
}
