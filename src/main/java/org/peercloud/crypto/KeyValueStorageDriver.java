package org.peercloud.crypto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/31/12
 * Time: 5:18 PM
 */
public interface KeyValueStorageDriver extends Iterable<String> {
    public void put(String key, String value);
    public String get(String key);
}
