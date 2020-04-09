package afip.cda.projet_crud.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import afip.cda.projet_crud.controleur.Controle;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.utilitaire.AccessHTTP;
import afip.cda.projet_crud.utilitaire.AsyncResponse;

public class AccesDistant implements AsyncResponse {

    private static final String SERVERADDR ="https://salimdev.alwaysdata.net/serveurcoach.php";
    private Controle controle;

    public AccesDistant() {
        controle = Controle.getInstance(null);
    }

    /**
     * S'éxecute lors Retour du serveur distant!
     * c'est dans cette methode qu'on va pouvoir faire avec les infos du serveur distant
     * @param output
     */
    @Override
    public void processFinish(String output) {
        Log.d("serveur : ", "*********"+output);
        //decoupage du message reçu avec le charactere %
        String[] message = output.split("%");
        //dans le message[0] => "enreg", "dernier", "Erreur"
        //dans le message[1] => reste des messages

        // s'il y a 2 cases:
        if (message.length > 1){
            if (message[0].equals("enreg")){
                Log.i("enreg : ", "*********"+message[1]);
            }else{
                if (message.equals("dernier")){
                    Log.i("tests : ", "****Connection*****"+message[1]);
                    try {
                        JSONObject info = new JSONObject(message[1]);
                        String nom= info.getString("nom");
                        String prenom= info.getString("prenom");
                        String mdp= info.getString("mdp");
                        String mail= info.getString("email");
                        String path= info.getString("photo");
                        User user = new User(nom, prenom, mdp, mail, path);
                        controle.setUser(user);
                    } catch (JSONException e) {
                        Log.i("erreur JSON : ", "Conversion ********* JSON IMPOSSIBLE "+e.toString());
                    }
                }else{
                    if (message.equals("Erreur")){
                        Log.i("Erreur : ", "*********"+message[1]);
                    }
                }
            }
        }

    }

    public void envoie(String operation, JSONArray lesdonneesJSON){
        AccessHTTP accesDonnees = new AccessHTTP();
        //liens de délagation
        accesDonnees.delegate = this;

        //Ajout de parametres
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("lesdonees", lesdonneesJSON.toString());

        //Appel au serveur distant
        accesDonnees.execute(SERVERADDR);
    }
}
