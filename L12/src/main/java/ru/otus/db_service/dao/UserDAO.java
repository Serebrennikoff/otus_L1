package ru.otus.db_service.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.db_service.data_sets.UserDataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Data access object for user entity.
 */
public class UserDAO {
    private Session session;

    public UserDAO(Session session) {this.session = session;}

    public long save(UserDataSet user) {return (long)session.save(user);}

    public UserDataSet find(long id) {return session.get(UserDataSet.class, id);}

    public UserDataSet findByName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));
        Query<UserDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<UserDataSet> findAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);
        return session.createQuery(criteria).list();
    }
}
