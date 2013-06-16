package org.peercloud.dao;

import org.hibernate.SessionFactory;
import org.peercloud.persistence.Note;
import org.peercloud.persistence.PublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/15/13
 * Time: 4:48 AM
 */
@Repository
public class PublicKeyDAOImpl implements PublicKeyDAO {
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void add(PublicKey key) {
        sessionFactory.getCurrentSession().save(key);
    }

    @Override
    public void remove(PublicKey key) {
        PublicKey publicKey = (PublicKey) sessionFactory.getCurrentSession()
                .load(PublicKey.class, key.getFingerprint());
        if (null != publicKey) {
            sessionFactory.getCurrentSession().delete(publicKey);
        }
    }

    @Override
    public List<PublicKey> list() {
        return sessionFactory.getCurrentSession().createQuery("from PublicKey").list();
    }
}
