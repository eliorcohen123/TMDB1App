package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;

public class DataOfMovie extends AppCompatActivity {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_of_movie);

        final MovieModel item = (MovieModel) getIntent().getExtras().getSerializable(getString(R.string.movie_edit)); // GetSerializable for the texts

        final RadioGroup rg1 = findViewById(R.id.radioGroup2);  // ID of the RadioGroup2 of DataOfMovie
        final RadioButton rb1 = findViewById(R.id.radioButton3);  // ID of the RadioButton3 of DataOfMovie
        final RadioButton rb2 = findViewById(R.id.radioButton4);  // ID of the RadioButton4 of DataOfMovie

        // Checked if the RadioButton equal to 1 or 2
        if (item.getIs_watch() == 1) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }

        // Put the data of the checked RadioButton in SQLiteHelper
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton3) {
                    MediaPlayer sRadioButton = MediaPlayer.create(DataOfMovie.this, R.raw.radiobutton_sound);
                    sRadioButton.start();  // Play sound

                    item.setIs_watch(1);

                    MovieDBHelper movieDBHelper = new MovieDBHelper(DataOfMovie.this);
                    movieDBHelper.updateMovieIsWatch(item);
                } else if (checkedId == R.id.radioButton4) {
                    MediaPlayer sRadioButton = MediaPlayer.create(DataOfMovie.this, R.raw.radiobutton_sound);
                    sRadioButton.start();  // Play sound

                    item.setIs_watch(0);

                    mMovieDBHelper = new MovieDBHelper(DataOfMovie.this);
                    mMovieDBHelper.updateMovieIsWatch(item);
                }
            }
        });

        final TextView subject = findViewById(R.id.textView22);  // ID of the subject
        final TextView body = findViewById(R.id.textView23);  // ID of the body
        final TextView URL = findViewById(R.id.textView24);  // ID of the URL

        subject.setText(item.getTitle());  // GetSerializable of subject
        body.setText(item.getOverview());  // GetSerializable of body
        URL.setText(item.getPoster_path());  // GetSerializable of URL

        // Button that does the following:
        Button button1 = findViewById(R.id.button14);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sAdd = MediaPlayer.create(DataOfMovie.this, R.raw.add_and_edit_sound);
                sAdd.start();  // Play sound

                // Pass from DataOfMovie to MainActivity
                Intent intentEditToMain = new Intent(DataOfMovie.this, MainActivity.class);
                startActivity(intentEditToMain);
            }
        });

        //Show the ImageView
        String picture = "https://image.tmdb.org/t/p/w154" + item.getPoster_path();
        final ImageView imageView = findViewById(R.id.imageView2);
        Picasso.get().load(picture).into(imageView);

        ImageView buttonYou = findViewById(R.id.imageView6);
        buttonYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(DataOfMovie.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound
                try {
                    watchYoutubeVideo(item.getTitle() + " Trailer");
                } catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.youtube"));
                    startActivity(intent);
                }
            }
        });

        // Button are back to the previous activity
        Button button = findViewById(R.id.button15);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(DataOfMovie.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
            }
        });
    }

    private void watchYoutubeVideo(String nameQuery) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", nameQuery);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
