package secretar.aop;

import java.lang.reflect.Method;
import java.util.List;
import secretar.dao.DataStoreException;

public interface FinderExecutor<T> {
    public List<T> executeListFinder(Method method, final Object[] queryArgs) throws DataStoreException;

    T executeUniqueFinder(Method method, Object[] arguments) throws DataStoreException;
}
