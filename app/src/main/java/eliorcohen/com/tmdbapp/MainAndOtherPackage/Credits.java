package eliorcohen.com.tmdbapp.MainAndOtherPackage;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import eliorcohen.com.tmdbapp.R;

public class Credits extends AppCompatActivity {

    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        // Button are back to the previous activity
        buttonOK = findViewById(R.id.textViewOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(Credits.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
            }
        });
    }

}
