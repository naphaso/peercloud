package org.peercloud.dao;

import org.hibernate.SessionFactory;
import org.peercloud.persistence.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/16/13
 * Time: 10:24 AM
 */
public interface NoteDAO {
    public void addNote(Note note);
    public List<Note> listNote();
    public void removeNote(Integer id);
}
