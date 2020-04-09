package afip.cda.projet_crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import afip.cda.projet_crud.dao.UserDao;
import afip.cda.projet_crud.db.User;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "tests";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // gestion list avec un recylceview
        RecyclerView recyclerview = findViewById(R.id.reclyeview);

        //1- Disposition item de la liste
        RecyclerView.LayoutManager lm = new LinearLayoutManager(ListActivity.this);
        recyclerview.setLayoutManager(lm);

        //2- Associer Adapter au recyclerview
        recyclerview.setAdapter(new MyRecyclerViewAdapter());


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            Log.i("tests", " id : " + id);
            Toast.makeText(getApplicationContext(), "**** "+ id+" *****", Toast.LENGTH_SHORT).show();
        }

    }

    private void openActivity(Class<?> activityClass) {
        //Creation Intent (explicite) pour lancer autre Activity.
        Intent intent = new Intent(this, activityClass);
        //Lancer Intent
        startActivity(intent);
    }

    private void openActivity(Class<?> activityclass, int id) {
        Intent intent;
        intent = new Intent(ListActivity.this, activityclass);
        intent.putExtra("id", id);
        //lacer intent
        startActivity(intent);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

        List<User> data = new UserDao(getApplicationContext()).all();

        public MyRecyclerViewAdapter() {

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Creation de viewHolder qui va contenir la precsentation chaque element de la liste
            View item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_layout, parent, false);
            return new MyViewHolder(item_view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            UserDao dao = new UserDao(getApplicationContext());
            holder.firstname.setText(data.get(position).getNom()); //injecter données dans item liste
            holder.lastname.setText(data.get(position).getPrenom());
            holder.email.setText(data.get(position).getEmail());
            File imgFile = new File(data.get(position).getPhoto());
            holder.img.setImageURI(Uri.fromFile(imgFile));

            View.OnClickListener listener = (v) -> {
                if (v.getId() == R.id.btn_delete) {
                    dao.delete(data.get(position).getId());
                    openActivity(ListActivity.class);
                }
                if (v.getId() == R.id.btn_update) {
                    openActivity(Create.class, data.get(position).getId());
                }
            };
            //associer au button
            if(id != data.get(position).getId()){
                holder.b1.setVisibility(View.INVISIBLE);
                holder.b2.setVisibility(View.INVISIBLE);
            }
            holder.b1.setOnClickListener(listener);
            holder.b2.setOnClickListener(listener);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            TextView firstname;
            TextView lastname;
            TextView email;
            ImageView img;

            Button b1;
            Button b2;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                firstname = itemView.findViewById(R.id.item_nom);
                lastname = itemView.findViewById(R.id.item_prenom);
                email = itemView.findViewById(R.id.item_mail);
                img = itemView.findViewById(R.id.img_img);
                b1 = itemView.findViewById(R.id.btn_delete);
                b2 = itemView.findViewById(R.id.btn_update);
            }
        }
    }
}
