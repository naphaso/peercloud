package org.peercloud.crypto;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/30/12
 * Time: 4:35 AM
 */
public class CertificateStorage {
    private static Logger logger = LoggerFactory.getLogger(CertificateStorage.class);
    private static CertificateStorage instance;
    private static String certsDir = "certs";
    // Fingerprint -> Certificate map
    HashMap<String, Certificate> certs = new HashMap<>();

    private KeyValueStorageDriver storageDriver;

    private CertificateStorage() {
        storageDriver = new FileKeyValueStorageDriver(certsDir);
        for(String s : storageDriver) {
            Certificate cert = new Certificate(s);
            if (cert.isValid()) {
                certs.put(cert.getFingerprint(), cert);
                logger.debug("certificate {} successfully loaded", cert.getName());
            } else {
                logger.error("error loading certificate {}", cert.getName());
            }
        }
    }

    public static void setCertsDir(String certsDir) {
        CertificateStorage.certsDir = certsDir;
        instance = null;
    }

    public static void reset() {
        instance = null;
    }

    public static synchronized CertificateStorage getInstance() {
        if (instance == null)
            instance = new CertificateStorage();
        return instance;
    }

    public List<Certificate> getCertificatesByName(String name) {
        ArrayList<Certificate> result = new ArrayList<>();
        for(Certificate cert : certs.values())
            if(cert.getName().equals(name))
                result.add(cert);
        return result;
    }

    public Certificate getCertificateByFingerprint(String fingerprint) {
        return certs.get(fingerprint);
    }

    public void saveCertificate(Certificate cert) {
        if (!certs.containsKey(cert.getFingerprint())) {
            certs.put(cert.getFingerprint(), cert);
            storageDriver.put(cert.getFingerprint(), cert.serialize(true));
        } else {
            // TODO: comparison of certs
        }
    }

    public void updateCertificate(Certificate cert) {
        if(certs.containsKey(cert.getFingerprint())) {
            storageDriver.put(cert.getFingerprint(), cert.serialize(true));
        } else {
            //logger.warn("try to update non-existing certificate");
        }
    }
}
