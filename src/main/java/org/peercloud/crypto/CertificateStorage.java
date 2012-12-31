package org.peercloud.crypto;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/30/12
 * Time: 4:35 AM
 */
public class CertificateStorage {
    private static Logger logger = LoggerFactory.getLogger(CertificateStorage.class);
    private static CertificateStorage instance;
    HashMap<String, Certificate> certs = new HashMap<>();

    private CertificateStorage() {
        Collection<File> listCerts = (Collection<File>) FileUtils.listFiles(new File("certs"),
                new RegexFileFilter("^[a-zA-Z0-9.]+\\.txt$"),
                new RegexFileFilter("^[a-zA-Z0-9.]+$"));

        for (File file : listCerts) {
            try {
                Certificate cert = new Certificate(file);
                logger.debug("load certificate {} with name {}", file.getName(), cert.getName());
                if (cert.isValid()) {
                    certs.put(cert.getName(), cert);
                    logger.debug("certificate {} successfully loaded", cert.getName());
                } else {
                    logger.error("error loading certificate {}", cert.getName());
                }
            } catch (IOException e) {
                logger.error("error reading certificate file", e);
            }
        }

    }

    public static synchronized CertificateStorage getInstance() {
        if (instance == null)
            instance = new CertificateStorage();
        return instance;
    }

    public Certificate getCertificate(String name) {
        return certs.get(name);
    }

    public void saveCertificate(Certificate cert) {
        if (certs.containsKey(cert.getName())) {
            certs.put(cert.getName(), cert);
            try {
                FileWriter fw = new FileWriter("certs/" + cert.getName() + ".txt");
                fw.write(cert.serialize());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // TODO: comparison of certs
        }
    }
}
