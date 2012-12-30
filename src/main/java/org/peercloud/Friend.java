package org.peercloud;

import com.thoughtworks.xstream.annotations.*;
import org.peercloud.network.F2FConnection;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 12/29/12
 * Time: 9:16 AM
 */

@XStreamAlias("friend")
public class Friend {
    @XStreamAsAttribute()
    @XStreamAlias("name")
    private String name;
    @XStreamAlias("host")
    private String host;
    @XStreamAlias("fingerprint")
    private String fingerprint;

    @XStreamOmitField
    private F2FConnection connection;

    public Friend(String name, String host, String fingerprint) {
        this.name = name;
        this.host = host;
        this.fingerprint = fingerprint;
    }
}
