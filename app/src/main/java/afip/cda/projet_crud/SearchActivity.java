package afip.cda.projet_crud;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import afip.cda.projet_crud.dao.UserDao;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.model.Book;


public class SearchActivity extends Activity {
    private int id;
    private EditText search;
    private Button btn_search;
    private AlertDialog.Builder builder;

    private RequestQueue mRequestQueue;
    private String url = "https://www.googleapis.com/books/v1/volumes?q=";
    private ProgressBar pgbar;
    public static List<Book> list = new ArrayList<Book>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btn_search = findViewById(R.id.btn_recherche);
        pgbar = findViewById(R.id.progressBar);
        pgbar.setVisibility(View.GONE);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pgbar.setVisibility(View.VISIBLE);
              sendAndRequestResponse();
            }
        }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            UserDao dao = new UserDao(getApplicationContext());
            id = bundle.getInt("id");
            User user = dao.find(id);
        }
        btn_search = findViewById(R.id.btn_recherche);
    }

    private void sendAndRequestResponse() {
        search = findViewById(R.id.ett_search);
        String contentSearch = search.getText().toString();
        if (contentSearch.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez saisir une recherche", Toast.LENGTH_LONG).show();//display the response on screen
            openActivity(SearchActivity.class);
            return;
        }
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url + contentSearch, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String title, auteur, image, isbn;
                        try {
                            JSONArray jArray = response.getJSONArray("items");
                            if (list.size() != 0)
                                list = new ArrayList<Book>();
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    if (jArray.getJSONObject(i).getJSONObject("volumeInfo").get("title") != null) {
                                        title = jArray.getJSONObject(i).getJSONObject("volumeInfo").get("title").toString();
                                    } else
                                        title = "null";
                                    if (jArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors") != null) {
                                        int u = 0;
                                        auteur = "";
                                        while(jArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors").length() != u){
                                            if(u >= 1) {
                                                auteur += " ,";
                                            }
                                            auteur += jArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors").get(u).toString();
                                            u++;
                                        }
                                    }else auteur = "null";
                                    if (jArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").get("thumbnail") != null) {
                                        image = jArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").get("thumbnail").toString();
                                    } else
                                        image = "null, ";
                                    if (jArray.getJSONObject(i).getString("id") != null) {
                                        isbn = jArray.getJSONObject(i).getString("id") ;
                                    } else
                                        isbn = "null";

                                    Book book = new Book(isbn, title, auteur, image);
                                    list.add(book);
                                } catch (Exception e) {
                                    Log.i("tests", "ERReur : " + e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(SearchActivity.this, "Erreur :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pgbar.setVisibility(View.GONE);
                        RecyclerView recyclerview = findViewById(R.id.recycler_view_book);
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(SearchActivity.this);
                        recyclerview.setLayoutManager(lm);
                        recyclerview.setAdapter(new BooksAdaptater(getApplicationContext(), list));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tests", "Error :" + error.toString());
                    }
                });
        mRequestQueue.add(jsObjRequest);
    }

    private void update() {
        openActivity(Create.class, id);
    }

    private void delete() {
        UserDao dao = new UserDao(getApplicationContext());
        dao.delete(id);
    }

    public boolean onOptionsItemSelected(MenuItem item, View view) {
        switch (item.getItemId()) {
            case R.id.item_update:
                update();
                return true;
            case R.id.item_delete:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("message").setTitle("Supprimmer?");
                builder.setMessage("Voulez-vous supprimmer votre Compte ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete();
                                openActivity(LoginActivity.class);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete?");
                alert.show();
                return true;
        }
        ;
        return true;
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void openActivity(Class<?> activityclass, int id) {
        Intent intent;
        intent = new Intent(SearchActivity.this, activityclass);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }
}
