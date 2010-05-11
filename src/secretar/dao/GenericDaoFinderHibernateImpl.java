package secretar.dao;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import secretar.aop.FinderExecutor;

@Transactional(propagation = Propagation.REQUIRED)
public class GenericDaoFinderHibernateImpl<T, PK extends Serializable> extends HibernateDaoSupport implements GenericDao<T, PK>, FinderExecutor<T> {
    private Class<T> type;

    private static Logger logger = Logger.getLogger(GenericDaoFinderHibernateImpl.class);

    public GenericDaoFinderHibernateImpl(Class<T> type) {
        this.type = type;
    }

    public T add(T o) {
         return read(create(o));
    }

    public PK create(T o) {
         PK pk = (PK) getHibernateTemplate().save(o);
         return pk;
    }

    public T read(PK id) {
        T o = (T) getHibernateTemplate().load(type, id);
        return o;
    }

    public void update(T o) {
        getHibernateTemplate().update(o);
    }

    public void delete(T o) {
        getHibernateTemplate().delete(o);
    }

    public List<T> list() {
        return getHibernateTemplate().loadAll(type);
    }

    public void deleteAll() {
        getHibernateTemplate().deleteAll(list());
    }

    @Override
    public List<T> executeListFinder(Method method, final Object[] queryArgs) throws DataStoreException {
        try {
            final String queryName = queryNameFromMethod(method);
            return (List<T>) getHibernateTemplate().findByNamedQuery(queryName, queryArgs);
        } catch (Throwable e) {
            logger.error(e.getMessage());
            throw new DataStoreException(e);
        }
    }

    @Override
    public T executeUniqueFinder(final Method method,final Object[] queryArgs) throws DataStoreException {
        try {
            final String queryName = queryNameFromMethod(method);
            List<T> list = (List<T>)getHibernateTemplate().findByNamedQuery(queryName, queryArgs);
            return (list != null && !list.isEmpty()) ? list.get(0) : null;
        } catch (Throwable e) {
            logger.error(e.getMessage());
            throw new DataStoreException(e);
        }

    }

    private String queryNameFromMethod(Method finderMethod) {
        return type.getSimpleName() + "." + finderMethod.getName();
    }

}

