package afip.cda.projet_crud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import afip.cda.projet_crud.dao.UserDao;
import afip.cda.projet_crud.utilitaire.Utilitaire;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAccount();
    }

    private void createAccount() {
        TextView create = findViewById(R.id.create);
        Button login = findViewById(R.id.btn_login);
        TextView email = findViewById(R.id.txt_mail_login);
        TextView pass = findViewById(R.id.txt_pass_login);

        View.OnClickListener listener = (v) -> {
            if (v.getId() == R.id.create) {
                openActivity(Create.class);
            }if(v.getId() == R.id.btn_login){
                UserDao dao = new UserDao(getApplicationContext());
                try {
                    if(dao.find(email.getText().toString(), pass.getText().toString() ) != null){
                        Intent ii=new Intent(LoginActivity.this, SearchActivity.class);
                        ii.putExtra("id", dao.find(email.getText().toString()).getId());
                        startActivity(ii);
                        Toast.makeText(this, "Réussi", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Erreur de Login Veuillez recommencer.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e)
                {
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };
        //associer au button
        create.setOnClickListener(listener);
        login.setOnClickListener(listener);
    }
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
