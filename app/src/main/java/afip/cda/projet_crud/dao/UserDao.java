package afip.cda.projet_crud.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import afip.cda.projet_crud.db.DbManager;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.utilitaire.Utilitaire;

public class UserDao implements  IDao<User> {
    private static final String TAG = "tests";
    DbManager database;
    Dao<User, Integer> dao;
    Dao<User, String> userDao;
    Dao<User, Object> userObj;

    public UserDao(Context context) {
        try {
            database = new DbManager(context);
            dao =  database.getDao(User.class);
            userDao = database.getDao(User.class);
            userObj = database.getDao(User.class);
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
    }

    @Override
    public List<User> all() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return null;
    }

    @Override
    public User find(int id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return null;
    }

    public User find(String email, String pass) {
            QueryBuilder<User, String> qb = userDao.queryBuilder();
            Where where = qb.where();
        Utilitaire uti = new Utilitaire();
            try {
                try{
                    pass = uti.SHA1(pass);
                }catch(Exception e){
                    Log.i("tests", "Erreur "+e.getMessage());
                }
                where.and(
                where.eq(User.FIELD_EMAIL, email),
                where.eq(User.FIELD_PASSWORD, pass));
                return qb.queryForFirst();

            } catch (SQLException e) {
                Log.i("tests", "Erreur "+e.getMessage());
            }
        return null;
    }

    public User find(String email) {
        QueryBuilder<User, String> qb = userDao.queryBuilder();
        Where where = qb.where();
        try {
            where.eq(User.FIELD_EMAIL, email);
            return qb.queryForFirst();

        } catch (SQLException e) {
            Log.i("tests", "Erreur "+e.getMessage());
        }
        return null;
    }

    @Override
    public boolean add(User item) {
        try {
            return dao.create(item)>0;
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(User item) {
        try {
            return dao.update(item)>0;
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            return dao.deleteById(id)>0;
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(User item) {
        try {
            /**
             * on peut aussi faire:
             * User u = find(id);
             * if(u!=null)
             *    return dao.delete(u)>0;
             */
            return dao.delete(item)>0;
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return false;
    }

    public User getLastInsert(){
        try {
            return dao.queryForAll().get(dao.queryForAll().size() - 1);
        } catch (SQLException e) {
            Log.i(TAG, "UserDao; "+e.getMessage());
        }
        return null;
    }
}
