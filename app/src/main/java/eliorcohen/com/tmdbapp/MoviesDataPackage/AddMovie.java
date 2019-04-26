package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.R;

public class AddMovie extends AppCompatActivity {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie);

        final EditText subject = findViewById(R.id.editText1);  // ID of the subject
        final EditText body = findViewById(R.id.editText2);  // ID of the body
        final EditText URL = findViewById(R.id.editText3);  // ID of the URL

        // Button that does the following:
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
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
                    mMovieDBHelper = new MovieDBHelper(AddMovie.this);
                    mMovieDBHelper.addMovie(title, overview, url);

                    // Pass from AddMovie to MainActivity
                    Intent intentAddToMain = new Intent(AddMovie.this, MainActivity.class);
                    startActivity(intentAddToMain);
                }
            }
        });

        // Button to show the ImageView
        Button button2 = findViewById(R.id.button20);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sShowImage = MediaPlayer.create(AddMovie.this, R.raw.show_image_sound);
                sShowImage.start();  // Play sound

                URL.setVisibility(View.INVISIBLE);  // Canceling the show of URL

                //Initialize the ImageView
                String picture = "https://image.tmdb.org/t/p/w154" + URL.getText().toString();  // GetText of the URL
                final ImageView imageView = findViewById(R.id.imageView4);
                Picasso.get().load(picture).into(imageView);
            }
        });


        // Button are back to the previous activity
        Button button3 = findViewById(R.id.button2);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(AddMovie.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
            }
        });
    }

}
