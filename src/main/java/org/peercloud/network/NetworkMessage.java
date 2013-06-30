package org.peercloud.network;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/30/13
 * Time: 12:19 AM
 */
public class NetworkMessage {
    private byte[] data;

    public NetworkMessage(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
