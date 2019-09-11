package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;

public class AddMovieFromInternet extends AppCompatActivity implements View.OnClickListener {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app
    private MovieModel item;
    private TextView textViewOK, textViewShow;
    private EditText subject, body, URL;
    private Button btnBack;
    private ImageView imageView;
    private MediaPlayer sError, sAdd, sShowImage, sCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie_from_internet);

        initUI();
        getData();
        initListeners();
    }

    private void initUI() {
        // GetSerializable for the texts
        item = (MovieModel) getIntent().getExtras().getSerializable(getString(R.string.movie_add_from_internet));

        subject = findViewById(R.id.editTextSubject);  // ID of the subject
        body = findViewById(R.id.editTextBody);  // ID of the body
        URL = findViewById(R.id.editTextURL);  // ID of the URL

        textViewOK = findViewById(R.id.textViewOK);
        textViewShow = findViewById(R.id.textViewShow);

        imageView = findViewById(R.id.imageView3);

        btnBack = findViewById(R.id.btnBack);

        mMovieDBHelper = new MovieDBHelper(this);

        sError = MediaPlayer.create(AddMovieFromInternet.this, R.raw.error_sound);
        sAdd = MediaPlayer.create(AddMovieFromInternet.this, R.raw.add_and_edit_sound);
        sShowImage = MediaPlayer.create(AddMovieFromInternet.this, R.raw.show_image_sound);
        sCancel = MediaPlayer.create(AddMovieFromInternet.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        textViewOK.setOnClickListener(this);
        textViewShow.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void getData() {
        assert item != null;  // If the item of subject not null
        subject.setText(item.getTitle());  // GetSerializable of subject
        body.setText(item.getOverview());  // GetSerializable of body
        URL.setText(item.getPoster_path());  // GetSerializable of URL

        //Initialize the ImageView
        String picture = "https://image.tmdb.org/t/p/w154" + item.getPoster_path();
        Picasso.get().load(picture).into(imageView);
        imageView.setVisibility(View.INVISIBLE); //Set the ImageView Invisible
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewOK:
                String title = subject.getText().toString();  // GetText of the subject
                String overview = body.getText().toString();  // GetText of the body
                String url = URL.getText().toString();  // GetText of the URL

                if (TextUtils.isEmpty(subject.getText())) { // If the text are empty the movie will not be approved
                    sError.start();  // Play sound

                    subject.setError("Title is required!");  // Print text of error if the text are empty
                } else {
                    sAdd.start();  // Play sound

                    // The texts in the SQLiteHelper
                    mMovieDBHelper.addMovie(title, overview, url);

                    // Pass from AddMovieFromInternet to MainActivity
                    Intent intentAddInternetToMain = new Intent(AddMovieFromInternet.this, MainActivity.class);
                    startActivity(intentAddInternetToMain);
                }
                break;
            case R.id.textViewShow:
                sShowImage.start();  // Play sound

                URL.setVisibility(View.INVISIBLE);  // Canceling the show of URL
                imageView.setVisibility(View.VISIBLE);  // Show the ImageView
                break;
            case R.id.btnBack:
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
