package org.peercloud;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.peercloud.crypto.Certificate;
import org.peercloud.crypto.CertificateFactory;
import org.peercloud.crypto.CertificateStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 1/4/13
 * Time: 6:23 AM
 */
public class CertificatesTest {
    static Logger logger = LoggerFactory.getLogger(CertificatesTest.class);
    private Path testdir;
    @Before
    public void prepareTest() {
        try {
            testdir = Files.createTempDirectory("test");
            CertificateStorage.setCertsDir(testdir.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanUp() {
        try {
            FileUtils.deleteDirectory(testdir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cryptoTest1() {
        logger.info("generating certificates...");
        CertificateFactory certificateFactory = CertificateFactory.getInstance();
        Certificate alice = certificateFactory.generate("alice");
        Certificate bob   = certificateFactory.generate("bob");
        Certificate eve   = certificateFactory.generate("eve");

        logger.info("alice fingerprint: {}", alice.getFingerprint());
        logger.info("bob fingerprint: {}", bob.getFingerprint());
        logger.info("eve fingerprint: {}", eve.getFingerprint());

        logger.info("store certificates...");
        CertificateStorage certificateStorage = CertificateStorage.getInstance();
        certificateStorage.saveCertificate(alice);
        certificateStorage.saveCertificate(bob);
        certificateStorage.saveCertificate(eve);

        logger.info("signing bob by alice...");
        Certificate.Sign bobSign = bob.signCertificate(alice, "", "");
        logger.info("signing alice by bob");
        Certificate.Sign aliceSign = alice.signCertificate(bob, "", "");
        logger.info("add fake alice sign to eve");
        eve.addSignature(aliceSign);


        logger.info("reset storage and reload certificates...");
        CertificateStorage.reset();
        certificateStorage = CertificateStorage.getInstance();

        logger.info("check alice signs...");
        certificateStorage.getCertificateByFingerprint(alice.getFingerprint()).checkSigns();
        logger.info("check bob signs...");
        certificateStorage.getCertificateByFingerprint(bob.getFingerprint()).checkSigns();
        logger.info("check eve signs...");
        certificateStorage.getCertificateByFingerprint(eve.getFingerprint()).checkSigns();
    }
}
