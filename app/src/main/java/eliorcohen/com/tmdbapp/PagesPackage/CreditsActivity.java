package eliorcohen.com.tmdbapp.PagesPackage;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import eliorcohen.com.tmdbapp.R;

public class CreditsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonOK;
    private MediaPlayer sCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        initUI();
        initListeners();
    }

    private void initUI() {
        buttonOK = findViewById(R.id.textViewOK);

        sCancel = MediaPlayer.create(CreditsActivity.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        buttonOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewOK:
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
