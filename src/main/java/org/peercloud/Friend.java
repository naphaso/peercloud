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
    public Status getStatus() {
        return status;
    }

    public enum Status {
        UNKNOWN, ONLINE, OFFLINE, REJECTED
    }

    @XStreamAsAttribute()
    @XStreamAlias("description")
    private String description;

    @XStreamAlias("fingerprint")
    private String fingerprint;

    @XStreamOmitField
    private F2FConnection connection;
    @XStreamOmitField
    private Status status;

    public Friend(String name, String host, String fingerprint) {
        this.description = description;
        this.fingerprint = fingerprint;
        this.status = Status.UNKNOWN;
    }

    public String getDescription() {
        return description;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void handleConnected(F2FConnection connection) {
        this.connection = connection;
        this.status = Status.ONLINE;
    }

    public void handleDisconnect() {
        status = Status.UNKNOWN;
    }

    public void handleReject() {
        status = Status.REJECTED;
    }

    public void handleRefused() {
        status = Status.OFFLINE;
    }
}
