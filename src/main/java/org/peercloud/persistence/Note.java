package org.peercloud.persistence;


import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/16/13
 * Time: 10:26 AM
 */
@Entity
@Table
public class Note {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Column
    private String content;

    @Column(length = 5000)
    byte[] data;

}
