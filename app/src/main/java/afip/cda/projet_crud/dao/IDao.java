package afip.cda.projet_crud.dao;

import java.util.List;

public interface IDao<T> {
    /**
     * Crud
     * @return
     */
    List<T> all();
    T find(int id);
    boolean add(T item);
    boolean update(T item);
    boolean delete(int id);
    boolean delete(T item);
}
