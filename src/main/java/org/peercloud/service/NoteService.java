package org.peercloud.service;

import org.peercloud.persistence.Note;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/16/13
 * Time: 10:52 AM
 */

public interface NoteService {
    public void addNote(Note contact);
    public List<Note> listNote();
    public void removeNote(Integer id);
    public Note getByData(byte[] data);
}
