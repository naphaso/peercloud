package org.peercloud.crypto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/30/12
 * Time: 4:35 AM
 */
public class CertificateStorage {
    private static CertificateStorage instance;

    public static synchronized CertificateStorage getInstance() {
        if(instance == null)
            instance = new CertificateStorage();
        return instance;
    }

    HashMap<String, Certificate> certs = new HashMap<>();

    public Certificate getCertificate(String name) {
        return certs.get(name);
    }

    public void saveCertificate(Certificate cert) {
        if(certs.containsKey(cert.getName())) {
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
