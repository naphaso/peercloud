package org.peercloud.service;

import org.hibernate.SessionFactory;
import org.peercloud.dao.NoteDAO;
import org.peercloud.persistence.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 2/16/13
 * Time: 10:53 AM
 */

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteDAO noteDAO;

    @Override @Transactional
    public void addNote(Note note) {
        noteDAO.addNote(note);
    }

    @Override @Transactional
    public List<Note> listNote() {
        return noteDAO.listNote();
    }

    @Override @Transactional
    public void removeNote(Integer id) {
        noteDAO.removeNote(id);
    }

    @Override @Transactional
    public Note getByData(byte[] data) {
        return noteDAO.getByData(data);
    }
}
