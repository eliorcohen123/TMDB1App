package eliorcohen.com.tmdbapp.PagesPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import eliorcohen.com.tmdbapp.R;

public class DeleteMovieActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBack;
    private MediaPlayer sOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_movie);

        initUI();
        initListeners();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btnBack);

        sOk = MediaPlayer.create(DeleteMovieActivity.this, R.raw.ok_sound);
    }

    private void initListeners() {
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                sOk.start();  // Play sound

                Intent intent = new Intent(DeleteMovieActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}
