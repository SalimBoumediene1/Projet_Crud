package afip.cda.projet_crud.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DbManager extends OrmLiteSqliteOpenHelper {

    private static final String databaseName = "db_cda";
    //private static final SQLiteDatabase.CursorFactory factory = null;
    private static final int databaseVersion = 2;
    private static final String TAG = "DbManager";

    //creation base de donnée
    public DbManager(Context context) {
        super(context, databaseName, null, databaseVersion);
        Log.i(TAG, "***********************************");
        Log.i(TAG, "onCreate: database create!");
        Log.i(TAG, "***********************************");
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            Log.i(TAG, "onCreate: table for 'User.class' created!");
        }catch (SQLException e){
            Log.i(TAG, "Erreur : " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
//            supprimer objet DB (tables...)
            TableUtils.dropTable(connectionSource, User.class, true);
            Log.i(TAG, "onUpdate: table for 'User.class' created!");

            //Rappeler la creation de la DB
            onCreate(database, connectionSource);
        }catch (SQLException e){
            Log.i(TAG, "Erreur : " + e.getMessage(), e);
        }
    }
}
