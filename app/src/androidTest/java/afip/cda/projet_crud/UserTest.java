package afip.cda.projet_crud;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import afip.cda.projet_crud.dao.UserDao;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.utilitaire.Utilitaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class UserTest {
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
