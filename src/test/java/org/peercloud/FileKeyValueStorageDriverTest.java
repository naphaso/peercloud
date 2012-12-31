package org.peercloud;


import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.peercloud.crypto.FileKeyValueStorageDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/31/12
 * Time: 7:12 PM
 */

public class FileKeyValueStorageDriverTest {
    private Path testdir;
    @Before
    public void prepareTest() {
        try {
            testdir = Files.createTempDirectory("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        FileKeyValueStorageDriver driver = new FileKeyValueStorageDriver(testdir.toString());

        driver.put("test1", "data1");
        driver.put("test2", "data2");
        driver.put("test3", "data3");
        driver.put("test2", "data4");

        Assert.assertEquals("test1", driver.get("test1"), "data1");
        Assert.assertEquals("test2", driver.get("test2"), "data4");
        Assert.assertEquals("test3", driver.get("test3"), "data3");
    }

    @After
    public void cleanup() {
        try {
            FileUtils.deleteDirectory(testdir.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
