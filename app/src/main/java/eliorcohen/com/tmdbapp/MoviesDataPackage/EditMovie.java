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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.picasso.Picasso;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;

public class EditMovie extends AppCompatActivity {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_movie);

        final int id = getIntent().getExtras().getInt(getString(R.string.movie_id)); // GetSerializable for the ID
        final MovieModel item = (MovieModel) getIntent().getExtras().getSerializable(getString(R.string.movie_edit)); // GetSerializable for the texts

        final RadioGroup rg1 = findViewById(R.id.radioGroup1);  // ID of the RadioGroup1 of EditMovie
        final RadioButton rb1 = findViewById(R.id.radioButton1);  // ID of the RadioButton1 of EditMovie
        final RadioButton rb2 = findViewById(R.id.radioButton2);  // ID of the RadioButton2 of EditMovie

        // Checked if the RadioButton equal to 1 or 2
        assert item != null;
        if (item.getIs_watch() == 1) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }

        // Put the data of the checked RadioButton in SQLiteHelper
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    MediaPlayer sRadioButton = MediaPlayer.create(EditMovie.this, R.raw.radiobutton_sound);
                    sRadioButton.start();  // Play sound

                    item.setIs_watch(1);

                    MovieDBHelper movieDBHelper = new MovieDBHelper(EditMovie.this);
                    movieDBHelper.updateMovieIsWatch(item);
                } else if (checkedId == R.id.radioButton2) {
                    MediaPlayer sRadioButton = MediaPlayer.create(EditMovie.this, R.raw.radiobutton_sound);
                    sRadioButton.start();  // Play sound

                    item.setIs_watch(0);

                    mMovieDBHelper = new MovieDBHelper(EditMovie.this);
                    mMovieDBHelper.updateMovieIsWatch(item);
                }
            }
        });

        final EditText subject = findViewById(R.id.editText5);  // ID of the subject
        final EditText body = findViewById(R.id.editText6);  // ID of the body
        final EditText URL = findViewById(R.id.editText7);  // ID of the URL

        subject.setText(item.getTitle());  // GetSerializable of subject
        body.setText(item.getOverview());  // GetSerializable of body
        URL.setText(item.getPoster_path());  // GetSerializable of URL

        // Button that does the following:
        Button button1 = findViewById(R.id.button8);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = subject.getText().toString();  // GetText of the subject
                String overview = body.getText().toString();  // GetText of the body
                String url = URL.getText().toString();  // GetText of the URL

                if (TextUtils.isEmpty(subject.getText())) {  // If the text are empty the movie will not be approved
                    MediaPlayer sError = MediaPlayer.create(EditMovie.this, R.raw.error_sound);
                    sError.start();  // Play sound

                    subject.setError("Title is required!");  // Print text of error if the text are empty
                } else {
                    MediaPlayer sAdd = MediaPlayer.create(EditMovie.this, R.raw.add_and_edit_sound);
                    sAdd.start();  // Play sound

                    // The texts in the SQLiteHelper
                    mMovieDBHelper = new MovieDBHelper(EditMovie.this);
                    mMovieDBHelper.updateMovie(title, overview, url, id);

                    // Pass from EditMovie to MainActivity
                    Intent intentEditToMain = new Intent(EditMovie.this, MainActivity.class);
                    startActivity(intentEditToMain);
                }
            }
        });

        //Initialize the ImageView
        String picture = "https://image.tmdb.org/t/p/w154" + item.getPoster_path();
        final ImageView imageView = findViewById(R.id.imageView);
        Picasso.get().load(picture).into(imageView);
        imageView.setVisibility(View.INVISIBLE); //Set the ImageView Invisible

        // Button to show the ImageView
        Button button2 = findViewById(R.id.button11);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sShowImage = MediaPlayer.create(EditMovie.this, R.raw.show_image_sound);
                sShowImage.start();  // Play sound

                imageView.setVisibility(View.VISIBLE);
            }
        });

        // Button are back to the previous activity
        Button button4 = findViewById(R.id.button9);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(EditMovie.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
            }
        });
    }

}
