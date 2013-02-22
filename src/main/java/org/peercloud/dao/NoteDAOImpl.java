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
 * Time: 10:39 AM
 */
@Repository
public class NoteDAOImpl implements NoteDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addNote(Note note) {
        sessionFactory.getCurrentSession().save(note);
    }

    @Override
    public List<Note> listNote() {
        return sessionFactory.getCurrentSession().createQuery("from Note").list();
    }

    @Override
    public void removeNote(Integer id) {
        Note note = (Note) sessionFactory.getCurrentSession()
                .load(Note.class, id);
        if (null != note) {
            sessionFactory.getCurrentSession().delete(note);
        }
    }
}
