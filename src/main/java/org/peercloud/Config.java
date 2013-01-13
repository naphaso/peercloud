package org.peercloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/23/12
 * Time: 4:12 AM
 */
public class Config {
    static final Logger logger = LoggerFactory.getLogger(Config.class);

    public List<InetAddress> getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(List<InetAddress> bootstrap) {
        this.bootstrap = bootstrap;
    }

    List<InetAddress> bootstrap = new ArrayList<InetAddress>();
    String basedir;

    public String getBasedir() {
        return basedir;
    }

    public void load(String filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Map<String, Object> doc = (Map<String, Object>) yaml.load(new FileInputStream(filename));

        List<String> bootstrap_conf = (List<String>) doc.get("bootstrap");
        for(String addr : bootstrap_conf)
            try {
                bootstrap.add(InetAddress.getByName(addr));
            } catch (UnknownHostException e) {
                logger.warn("bootstrap unknown host: {}", addr);
            }

        basedir = (String) doc.get("basedir");

    }
}
