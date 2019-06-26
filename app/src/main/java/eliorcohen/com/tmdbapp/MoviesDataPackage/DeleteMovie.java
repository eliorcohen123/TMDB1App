package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.R;

public class DeleteMovie extends AppCompatActivity {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_movie);

        initUI();
        btnBack();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void btnBack() {
        // A button are passes from DeleteMovie to MainActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sOk = MediaPlayer.create(DeleteMovie.this, R.raw.ok_sound);
                sOk.start();  // Play sound

                Intent intent = new Intent(DeleteMovie.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
