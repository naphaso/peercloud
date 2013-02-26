package org.peercloud.network.data;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class LinkID {
    private static final Logger logger = LoggerFactory.getLogger(LinkID.class);
    private byte[] link_id;
    boolean valid;

    public LinkID(byte[] link_id) {
        this.link_id = link_id;
        if(link_id.length == 10)
            valid = true;
        else
            valid = false;
    }

    public LinkID(String link_id) {
        try {
            this.link_id = Hex.decodeHex(link_id.toCharArray());
            if(this.link_id.length == 10)
               valid = true;
        } catch (DecoderException e) {
            logger.warn("incorrect link id '{}'", link_id);
            valid = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkID linkID = (LinkID) o;

        if (!Arrays.equals(link_id, linkID.link_id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(link_id);
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(link_id);
    }
}