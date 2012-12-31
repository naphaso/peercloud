package org.peercloud.crypto;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/31/12
 * Time: 5:21 PM
 */
public class FileKeyValueStorageDriver implements KeyValueStorageDriver {
    private static Logger logger = LoggerFactory.getLogger(FileKeyValueStorageDriver.class);

    private File directory;

    public FileKeyValueStorageDriver(String directory) {
        this.directory = new File(directory);
        if (!this.directory.isDirectory() || !this.directory.canWrite())
            logger.error("certificates directory not found or can't writable");
    }

    @Override
    public void put(String key, String value) {
        try {
            FileWriter fw = new FileWriter(new File(directory, key));
            fw.write(value);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String key) {
        try {
            File target = new File(directory, key);
            if(target.exists() && target.isFile() && target.canRead())
                return FileUtils.readFileToString(target);
            else
                logger.error("certificate reading error: {}", key);
        } catch (IOException e) {
            logger.error("error reading certificate {}:", key, e);
        }
        return "";
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            Iterator<File> fileIterator = ((Collection<File>) FileUtils.listFiles(directory,
                    new RegexFileFilter("^[a-zA-Z0-9.]+$"),
                    new RegexFileFilter("^[a-zA-Z0-9.]+$"))).iterator();

            @Override
            public boolean hasNext() {
                return fileIterator.hasNext();
            }

            @Override
            public String next() {
                try {
                    return FileUtils.readFileToString(fileIterator.next());
                } catch (IOException e) {
                    logger.error("error reading certificate");
                    return "";
                }
            }

            @Override
            public void remove() {}
        };
    }
}
