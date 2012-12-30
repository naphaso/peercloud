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
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
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


    String source;
    String name;
    RSAPublicKey publicKey;
    SortedMap<String, String> fields = new TreeMap<>();
    List<Sign> signs = new ArrayList<>();

    public Certificate(File file) throws IOException {
        this(FileUtils.readFileToString(file));
    }

    public Certificate(String source) {
        this.source = source;
        StringReader reader = new StringReader(source);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(":\\s*", 2);
                if (tokens.length == 2) {
                    if (tokens[0].equals("sign"))
                        signs.add(new Sign(tokens[1]));
                    else
                        fields.put(tokens[0], tokens[1]);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String publickeyParts[] = fields.get("publickey").split(":");
            BigInteger modulo = new BigInteger(publickeyParts[0], 16);
            BigInteger publicExponent = new BigInteger(publickeyParts[1], 16);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulo, publicExponent);
            KeyFactory fact = null;

            fact = KeyFactory.getInstance("RSA");

            publicKey = (RSAPublicKey) fact.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException e) {
            logger.error("RSA not found?", e);
        } catch (InvalidKeySpecException e) {
            logger.error("invalid public key", e);
        }

        name = fields.get("name");
        if(!name.matches("^[a-zA-Z0-9.]+$"))
            name = "InvalidName";
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private String getSignedString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SortedMap.Entry<String, String> e : fields.entrySet()) {
            stringBuilder.append(e.getKey());
            stringBuilder.append(':');
            stringBuilder.append(e.getValue());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public boolean checkSignature(Certificate cert, Sign signature) {
        if (!signature.checkValidity()) return false;
        try {
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(this.getPublicKey());
            verifier.update(cert.getSignedString().getBytes());
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

    public String serialize() {
        if(source != null)
            return source;
        return ""; // TODO: certificate serialization (on new certs)
    }

    public class Sign {

        String author;
        byte[] signature;
        long from;
        long to;

        public Sign(String source) {
            Matcher matcher = intervalPattern.matcher(source);
            if (matcher.find()) {
                try {
                    author = matcher.group(1);
                    from = dateToTimestamp(matcher.group(2));
                    to = dateToTimestamp(matcher.group(3));
                    signature = Hex.decodeHex(matcher.group(4).toCharArray());
                } catch (DecoderException e) {
                    logger.error("invalid signature hex string");
                }
            }
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
