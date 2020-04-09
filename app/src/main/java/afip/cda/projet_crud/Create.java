package afip.cda.projet_crud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.io.File;
import java.util.List;
import java.util.Map;

import afip.cda.projet_crud.controleur.Controle;
import afip.cda.projet_crud.dao.UserDao;
import afip.cda.projet_crud.db.User;
import afip.cda.projet_crud.myrequest.MyRequest;
import afip.cda.projet_crud.utilitaire.Utilitaire;
import afip.cda.projet_crud.volleySingleton.VolleySingleton;

public class Create extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String URL_PATH = "";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Controle controle;
    private RequestQueue queue;
    private MyRequest request;
    private static final String TAG = "DbManager";
    Utilitaire uti = new Utilitaire();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        gererBouton();
        verifyStoragePermissions(this);
        Intent intent = getIntent();
        UserDao dao = new UserDao(getApplicationContext());
        User user = dao.find(intent.getIntExtra("id", -1));

        this.controle = Controle.getInstance(getApplicationContext());
        if (user != null) {
            Button p1_button = (Button)findViewById(R.id.btn_inscrire);
            p1_button.setText("Modifier");
            display_user(user);
            gererBouton(user);
        }
    }

    private void display_user(User user) {
        TextView name = findViewById(R.id.txt_name_user);
        TextView lastname = findViewById(R.id.txt_user_lastname);
        TextView email = findViewById(R.id.txt_user_mail);
        ImageView myImage = (ImageView) findViewById(R.id.img_user);
        Intent intent = getIntent();
        UserDao dao = new UserDao(getApplicationContext());
        User userUpload = dao.find(intent.getIntExtra("id", -1));
        Log.i("tests", user.getNom());
        lastname.setText(user.getPrenom());
        email.setText(user.getEmail());
        name.setText(user.getNom());

        //display icon image with path internal storage !!!!
            File imgFile = new  File(user.getPhoto());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myImage.setImageBitmap(myBitmap);
            }
    }

    private void gererBouton() {
        Button b1 = findViewById(R.id.btn_uplaod);
        Button b2 = findViewById(R.id.btn_inscrire);
        TextView name = findViewById(R.id.txt_name_user);
        TextView lastname = findViewById(R.id.txt_user_lastname);
        TextView email = findViewById(R.id.txt_user_mail);
        TextView mdp = findViewById(R.id.txt_pass);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);


        View.OnClickListener listener = (v) -> {
            String message = "";
            Class<?> redirection = null;
            if (v.getId() == R.id.btn_uplaod) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
            if (v.getId() == R.id.btn_inscrire) {
                UserDao ud = new UserDao(this);
                if (name.getText().toString().matches("")) {
                    message = "Le Nom ne doit pas etre vide.";
                    redirection = Create.class;
                } else if (lastname.getText().toString().matches("")) {
                    message = "Le Prénom ne doit pas etre vide.";
                    redirection = Create.class;
                } else if (email.getText().toString().matches("")) {
                    message = "L'email ne doit pas etre vide.";
                    redirection = Create.class;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    message = "Mauvais format d'email.";
                    redirection = Create.class;
                }else if(ud.find(email.getText().toString()) != null){
                    message = "L'email existe déja.";
                    redirection = Create.class;
                }else if(uti.isValidPass(mdp.getText().toString()) == false){
                    message = "Mauvais format de mot de passe.";
                    redirection = Create.class;
                }
                else{
                    Utilitaire uti = new Utilitaire();
                    try {
                        User user = new User(name.getText().toString(), lastname.getText().toString(), email.getText().toString(),
                                "" + URL_PATH,  uti.SHA1(mdp.getText().toString()));
                        ud.add(user);
                        Intent ii = new Intent(Create.this, SearchActivity.class);
                        ii.putExtra("id", ud.getLastInsert().getId());
                        startActivity(ii);
                        message = "Création réussie!";
                        redirection = SearchActivity.class;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                openActivity(redirection);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            }
        };
        b1.setOnClickListener(listener);
        b2.setOnClickListener(listener);
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = findViewById(R.id.img_user);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);
            URL_PATH = getPath(selectedImage);
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void gererBouton(User user) {
        Button b1 = findViewById(R.id.btn_uplaod);
        Button b2 = findViewById(R.id.btn_inscrire);
        TextView name = findViewById(R.id.txt_name_user);
        TextView lastname = findViewById(R.id.txt_user_lastname);
        TextView email = findViewById(R.id.txt_user_mail);
        TextView mdp = findViewById(R.id.txt_pass);

        View.OnClickListener listener = (v) -> {
            if (v.getId() == R.id.btn_uplaod) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
            if (v.getId() == R.id.btn_inscrire) {
                UserDao ud = new UserDao(this);
                String message = "";
                Class<?> redirection = null;
                if (name.getText().toString().matches("")) {
                    message = "Le Nom ne doit pas etre vide.";
                    redirection = Create.class;
                } else if (lastname.getText().toString().matches("")) {
                    message = "Le Prénom ne doit pas etre vide.";
                    redirection = Create.class;
                } else if (email.getText().toString().matches("")) {
                    message = "L'email ne doit pas etre vide.";
                    redirection = Create.class;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    message = "Mauvais format d'email.";
                    redirection = Create.class;
                }else if(uti.isValidPass(mdp.getText().toString()) == false){
                    message = "Mauvais format de mot de passe.";
                    redirection = Create.class;
                } else {
                    user.setNom(name.getText().toString());
                    user.setPrenom(lastname.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPhoto(URL_PATH);
                    try {
                        user.setMdp(uti.SHA1(name.getText().toString()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    ud.update(user);
                    message = "Modification réussie!";
                    redirection = SearchActivity.class;
                    List<User> list = ud.all();
                }
                openActivity(redirection);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        };
        b1.setOnClickListener(listener);
        b2.setOnClickListener(listener);
    }

    public void recupUser(){
        TextView name = findViewById(R.id.txt_name_user);
        TextView lastname = findViewById(R.id.txt_user_lastname);
        TextView email = findViewById(R.id.txt_user_mail);
        TextView mdp = findViewById(R.id.txt_pass);
        ImageView myImage = (ImageView) findViewById(R.id.img_user);
        User user = new User();

        name.setText(user.getNom().toString());
        lastname.setText(user.getPrenom().toString());
        email.setText(user.getEmail().toString());
        mdp.setText(user.getMdp().toString());
        myImage.setImageURI(Uri.parse(user.getPhoto()));
    }
}


//this.controle.creerProfil(user);
//                        request.register(name.getText().toString(), lastname.getText().toString(), email.getText().toString(),
//                                "" + URL_PATH, uti.SHA1(mdp.getText().toString()).toString(), new MyRequest.RegisterCallback() {
//                                    @Override
//                                    public void onSuccess(String message) {
//                                        Toast.makeText(Create.this, ""+ message, Toast.LENGTH_SHORT).show();
//                                        openActivity(ListActivity.class);
//                                    }
//
//                                    @Override
//                                    public void inputErrors(Map<String, String> errors) {
//                                        Log.i("tests", "********************* : " + errors.get("email"));
//                                        if (errors.get("email") != null){
//                                            Toast.makeText(Create.this, ""+ errors.get("email"), Toast.LENGTH_SHORT).show();
//                                            Log.i("app", "************************* : " + errors.get("email"));
//                                            Log.i("app", "erreur : " + errors.get("email"));
//                                            Log.i("tests", "********************* : " + errors.get("email"));
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(String message) {
//                                        Log.i("app", "************************* : " );
//                                        Log.i("app", "erreur salim : " + message);
//                                        Log.i("app", "********************* : ");
//                                    }
//                                });