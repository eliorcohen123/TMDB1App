package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.R;

public class AddMovie extends AppCompatActivity {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app
    private EditText subject, body, URL;
    private TextView textViewOK;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie);

        initUI();
        putData();
        btnBack();
    }

    private void initUI() {
        subject = findViewById(R.id.editTextSubject);  // ID of the subject
        body = findViewById(R.id.editTextBody);  // ID of the body
        URL = findViewById(R.id.editTextURL);  // ID of the URL

        // Button that does the following:
        textViewOK = findViewById(R.id.textViewOK);

        btnBack = findViewById(R.id.btnBack);

        mMovieDBHelper = new MovieDBHelper(AddMovie.this);
    }

    private void putData() {
        textViewOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = subject.getText().toString();  // GetText of the subject
                String overview = body.getText().toString();  // GetText of the body
                String url = URL.getText().toString();  // GetText of the URL

                if (TextUtils.isEmpty(subject.getText())) {  // If the text are empty the movie will not be approved
                    MediaPlayer sError = MediaPlayer.create(AddMovie.this, R.raw.error_sound);
                    sError.start();  // Play sound

                    subject.setError("Title is required!");  // Print text of error if the text are empty
                } else {
                    MediaPlayer sAdd = MediaPlayer.create(AddMovie.this, R.raw.add_and_edit_sound);
                    sAdd.start();  // Play sound

                    // The texts in the SQLiteHelper
                    mMovieDBHelper.addMovie(title, overview, url);

                    // Pass from AddMovie to MainActivity
                    Intent intentAddToMain = new Intent(AddMovie.this, MainActivity.class);
                    startActivity(intentAddToMain);
                }
            }
        });
    }

    private void btnBack() {
        // Button are back to the previous activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(AddMovie.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
            }
        });
    }

}
