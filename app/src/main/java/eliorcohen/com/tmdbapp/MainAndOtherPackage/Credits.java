package eliorcohen.com.tmdbapp.MainAndOtherPackage;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import eliorcohen.com.tmdbapp.R;

public class Credits extends AppCompatActivity implements View.OnClickListener {

    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        initUI();
        initListeners();
    }

    private void initUI() {
        buttonOK = findViewById(R.id.textViewOK);
    }

    private void initListeners() {
        buttonOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewOK:
                MediaPlayer sCancel = MediaPlayer.create(Credits.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
