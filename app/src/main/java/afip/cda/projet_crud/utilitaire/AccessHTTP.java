package afip.cda.projet_crud.utilitaire;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AccessHTTP extends AsyncTask<String, Integer, Long> {

    private String ret = null;
    public AsyncResponse delegate = null;
    private ArrayList<NameValuePair> parametres;

    /**
     * Constructeur ki nous permettra d'envoyé des requetes
     */
    public AccessHTTP() {
        parametres = new ArrayList<NameValuePair>();
    }

    /**
     * Methode qui nous permet l'Ajout d'un param POST
     *
     * @param nom
     * @param valeur
     */
    public void addParam(String nom, String valeur) {
        parametres.add(new BasicNameValuePair(nom, valeur));
    }

    //Connection entache de fond dans un Thread séparé
    @Override
    protected Long doInBackground(String... strings) {
        HttpClient cnxHTTP = new DefaultHttpClient();
        HttpPost paramCnx = new HttpPost(strings[0]);

        try {
            //encodage des parametres
            paramCnx.setEntity(new UrlEncodedFormEntity(parametres));
            // connexion et envoie des parametres et attente des réponses
            HttpResponse reponse = cnxHTTP.execute(paramCnx);

            //transformation de la réponse
            ret = EntityUtils.toString(reponse.getEntity());
        } catch (UnsupportedEncodingException e) {
            Log.d("Erreur encodage", "*******" + e.toString());
        } catch (ClientProtocolException e) {
            Log.d("Erreur protocole", "*******" + e.toString());
        } catch (IOException e) {
            Log.d("Erreur I/O", "*******" + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result){
        delegate.processFinish(ret.toString());

    }
}
