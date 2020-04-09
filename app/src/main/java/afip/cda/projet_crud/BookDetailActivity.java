package afip.cda.projet_crud;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import afip.cda.projet_crud.model.Book;

public class BookDetailActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private String url = "https://www.googleapis.com/books/v1/volumes/", id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }
        sendAndRequestResponse();
    }

    private void sendAndRequestResponse() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url + id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            ImageView imaga = findViewById(R.id.img_view_book);
                            TextView title = findViewById(R.id.txt_book_title);
                            TextView author = findViewById(R.id.txt_book_author);
                            TextView date = findViewById(R.id.txt_book_date);
                            TextView description = findViewById(R.id.txt_book_descrption);
                            Button button = findViewById(R.id.btn_prix_book);
                            if (response.getJSONObject("volumeInfo").getJSONObject("imageLinks").get("thumbnail") != null) {
                                Picasso.with(getApplicationContext())
                                        .load(response.getJSONObject("volumeInfo").getJSONObject("imageLinks").get("thumbnail").toString())
                                        .into(imaga);
                            }
                            if (response.getJSONObject("volumeInfo").get("title") != null) {
                                title.setText(response.getJSONObject("volumeInfo").get("title").toString());
                            } else
                                title.setText("Null");
                            if (response.getJSONObject("volumeInfo").getJSONArray("authors") != null) {
                                String authors="";
                                int u = 0;
                                while(response.getJSONObject("volumeInfo").getJSONArray("authors").length() != u){
                                    if(u >= 1) {
                                        authors += " ,";
                                    }
                                    authors += response.getJSONObject("volumeInfo").getJSONArray("authors").get(u).toString();
                                    u++;
                                }
                                author.setText(authors);
                            }else author.setText("Null");
                            if (response.getJSONObject("volumeInfo").get("description") != null) {
                                description.setText(response.getJSONObject("volumeInfo").get("description").toString());
                            } else
                                description.setText("Null");
                            if (response.getJSONObject("saleInfo").getJSONObject("listPrice").get("amount") != null) {
                                button.setText("Acheter "+ response.getJSONObject("saleInfo").getJSONObject("listPrice").get("amount").toString()+" €");
                                Toast.makeText(BookDetailActivity.this, ""+response.getJSONObject("saleInfo").get("buyLink").toString()
                                        , Toast.LENGTH_SHORT).show();
                                button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View arg0) {
                                        Intent viewIntent =
                                                null;
                                        try {
                                            viewIntent = new Intent("android.intent.action.VIEW",
                                                    Uri.parse(response.getJSONObject("saleInfo").getString("buyLink")));
                                        } catch (JSONException e) {
                                            Log.i("test", "Errue de lien : " +e);
                                        }
                                        startActivity(viewIntent);
                                    }
                                });
                            } else
                                button.setText("Null");
                            if (response.getJSONObject("volumeInfo").getString("publishedDate") != null) {
                                date.setText(response.getJSONObject("volumeInfo").getString("publishedDate").toString());
                            } else
                                date.setText("Null");
                        } catch (JSONException e) {
                            Log.i("tests", "Error de valeur:" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tests", "Error :" + error.toString());
                    }

                });
        mRequestQueue.add(jsObjRequest);
    }
}
