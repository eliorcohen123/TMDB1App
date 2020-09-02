package eliorcohen.com.tmdbapp.PagesPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import eliorcohen.com.tmdbapp.ViewModelsPackege.MovieViewModelFavorites;
import eliorcohen.com.tmdbapp.OthersPackage.ConApp;
import eliorcohen.com.tmdbapp.R;

public class AddMovieActivity extends AppCompatActivity implements View.OnClickListener {

    private MovieViewModelFavorites movieViewModelFavorites;
    private EditText subject, body, URL;
    private TextView textViewOK;
    private Button btnBack;
    private MediaPlayer sCancel, sError, sAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        initUI();
        initListeners();
    }

    private void initUI() {
        subject = findViewById(R.id.editTextSubject);  // ID of the subject
        body = findViewById(R.id.editTextBody);  // ID of the body
        URL = findViewById(R.id.editTextURL);  // ID of the URL

        // Button that does the following:
        textViewOK = findViewById(R.id.textViewOK);

        btnBack = findViewById(R.id.btnBack);

        movieViewModelFavorites = new MovieViewModelFavorites(ConApp.getApplication());

        sCancel = MediaPlayer.create(AddMovieActivity.this, R.raw.cancel_and_move_sound);
        sError = MediaPlayer.create(AddMovieActivity.this, R.raw.error_sound);
        sAdd = MediaPlayer.create(AddMovieActivity.this, R.raw.add_and_edit_sound);
    }

    private void initListeners() {
        textViewOK.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                sCancel.start();  // Play sound

                onBackPressed();
                break;
            case R.id.textViewOK: // Put Data
                String title = subject.getText().toString();  // GetText of the subject
                String overview = body.getText().toString();  // GetText of the body
                String url = URL.getText().toString();  // GetText of the URL

                if (TextUtils.isEmpty(subject.getText())) {  // If the text are empty the movie will not be approved
                    sError.start();  // Play sound

                    subject.setError("Title is required!");  // Print text of error if the text are empty
                } else {
                    sAdd.start();  // Play sound

                    // The texts in the SQLiteHelper
                    movieViewModelFavorites.insertMovie(title, overview, url);

                    // Pass from AddMovie to MainActivity
                    Intent intentAddToMain = new Intent(AddMovieActivity.this, MainActivity.class);
                    startActivity(intentAddToMain);
                }
                break;
        }
    }

}
