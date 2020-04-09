package afip.cda.projet_crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import afip.cda.projet_crud.utilitaire.Utilitaire;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    RingProgressBar progressbar;
    Handler handler;
    int bar = 0;
    private static final String TAG = "tests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = findViewById(R.id.progressbar);
        ringProgress();
    }

    public void ringProgress() {
        progressbar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                Toast.makeText(MainActivity.this, "Bienvenue!", Toast.LENGTH_SHORT).show();
                openActivity(LoginActivity.class);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {
                    if (bar < 250) {
                        bar+=50;
                        progressbar.setProgress(bar);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(100);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void openActivity(Class<?> activityClass) {
        //Creation Intent (explicite) pour lancer autre Activity.
        Intent intent = new Intent(this, activityClass);
        //Lancer Intent
        startActivity(intent);

    }
}
