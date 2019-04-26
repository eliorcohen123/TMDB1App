package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import eliorcohen.com.tmdbapp.R;

public class DeleteMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_movie);

        // A button are passes from DeleteMovie to MainActivity
        Button button1 = findViewById(R.id.button13);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sOk = MediaPlayer.create(DeleteMovie.this, R.raw.ok_sound);
                sOk.start();  // Play sound

                onBackPressed();
            }
        });
    }

}
