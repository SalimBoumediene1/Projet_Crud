package afip.cda.projet_crud.myrequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest {

    private static final String TAG = "DbManager";
    private Context context;
    private RequestQueue queue;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void register(final String nom, final String prenom,
                         final String email, String path, final String mdp,
                         RegisterCallback callback){

        String url = "https://salimdev.alwaysdata.net/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject();
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        //l'inscription est good.
                        callback.onSuccess("Vous vous etes bien inscrit.");
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("email")){
                            errors.put("email", messages.getString("email"));
                        }
                        callback.inputErrors(errors);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof Network){
                    callback.onError("Impossible de se connecter");
                }else if(error instanceof VolleyError)
                callback.onError("Une errerur s'est produite");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("nom", nom);
                map.put("prenom", prenom);
                map.put("email", email);
                map.put("path", path);
                map.put("mdp", mdp);

                return map;
            }
        };
            queue.add(request);
    }

    public interface RegisterCallback{
        void  onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }
}
