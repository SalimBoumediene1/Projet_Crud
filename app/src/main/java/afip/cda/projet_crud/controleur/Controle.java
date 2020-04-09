package afip.cda.projet_crud.controleur;

import android.content.Context;

import org.json.JSONArray;

import afip.cda.projet_crud.Create;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.model.AccesDistant;

public final class Controle {

    private static Controle instance = null;
    private static User user;
    private static AccesDistant accesDistant;
    private static Context context;

    public Controle() {
        super();
    }

    public static final Controle getInstance(Context context){
        if (context != null){
            Controle.context = context;
        }
        if (Controle.instance == null){
            Controle.instance = new Controle();
            accesDistant = new AccesDistant();
            accesDistant.envoie("dernier", new JSONArray());
        }
        return Controle.instance;
    }

    public void creerProfil(User user){
//        User user = new User(nom, prenom, email, photo, mdp);
        accesDistant.envoie("enreg", user.convertToJSONArray());
    }

    public void setUser(User user){
        Controle.user = user;
        ((Create)context).recupUser();
    }
}
