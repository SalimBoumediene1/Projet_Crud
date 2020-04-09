package afip.cda.projet_crud;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.j256.ormlite.table.TableUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import afip.cda.projet_crud.dao.UserDao;
import afip.cda.projet_crud.db.DbManager;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.utilitaire.Utilitaire;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserDao dao = new UserDao(appContext.getApplicationContext());
        assertNotNull(dao);
        //dao.add(new User("salim", "sassou", "sassou@sa.sa", "sass", "sasss"));
        List<User> users = dao.all();

        if(dao.find("testlastinsert")!=null){
            dao.delete(dao.find("testlastinsert"));
            assertNull("user inexistant", dao.find("testlastinsert"));
        }
        dao.add(new User("testlastinsert", "testlastinsert", "testlastinsert",
                "testlastinsert","testlastinsert"));
        int lastinsert = dao.find("testlastinsert").getId();
        assertNotNull("User existant", dao.find("testlastinsert"));
       assertEquals(dao.find("testlastinsert").getId(), dao.getLastInsert().getId());

        assertEquals("afip.cda.projet_crud", appContext.getPackageName());

        assertFalse(new Utilitaire().isValidPass("aaaaaaaa"));
        assertTrue(new Utilitaire().isValidPass("Aaaaa1234!"));
    }
}
