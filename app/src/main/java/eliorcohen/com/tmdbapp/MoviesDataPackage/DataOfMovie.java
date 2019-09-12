package eliorcohen.com.tmdbapp.MoviesDataPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.R;

public class DataOfMovie extends AppCompatActivity implements View.OnClickListener {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app
    private MovieModel item;
    private RadioGroup rg1;
    private RadioButton rb1, rb2;
    private TextView subject, body, URL;
    private ImageView buttonYouTube;
    private Button btnBack;
    private MediaPlayer sRadioButton, sYouTube,  sCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_of_movie);

        initUI();
        initListeners();
        radioGroup();
        getData();
    }

    private void initUI() {
        item = (MovieModel) getIntent().getExtras().getSerializable(getString(R.string.movie_edit)); // GetSerializable for the texts

        rg1 = findViewById(R.id.radioGroup);  // ID of the RadioGroup of DataOfMovie
        rb1 = findViewById(R.id.radioButton1);  // ID of the RadioButton1 of DataOfMovie
        rb2 = findViewById(R.id.radioButton2);  // ID of the RadioButton2 of DataOfMovie

        buttonYouTube = findViewById(R.id.imageViewYouTube);
        btnBack = findViewById(R.id.btnBack);

        subject = findViewById(R.id.textViewSubject);  // ID of the subject
        body = findViewById(R.id.textViewBody);  // ID of the body
        URL = findViewById(R.id.textViewURL);  // ID of the URL

        mMovieDBHelper = new MovieDBHelper(this);

        sRadioButton = MediaPlayer.create(DataOfMovie.this, R.raw.radiobutton_sound);
        sYouTube = MediaPlayer.create(DataOfMovie.this, R.raw.cancel_and_move_sound);
        sCancel = MediaPlayer.create(DataOfMovie.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        buttonYouTube.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void radioGroup() {
        // Checked if the RadioButton equal to 1 or 2
        if (item.getIs_watch() == 1) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }

        // Put the data of the checked RadioButton in SQLiteHelper
        rg1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton1) {
                sRadioButton.start();  // Play sound

                item.setIs_watch(1);
            } else if (checkedId == R.id.radioButton2) {
                sRadioButton.start();  // Play sound

                item.setIs_watch(0);
            }
            mMovieDBHelper.updateMovieIsWatch(item);
        });
    }

    private void getData() {
        subject.setText(item.getTitle());  // GetSerializable of subject
        body.setText(item.getOverview());  // GetSerializable of body
        URL.setText(item.getPoster_path());  // GetSerializable of URL
    }

    private void watchYoutubeVideo(String nameQuery) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", nameQuery);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewYouTube:
                sYouTube.start();  // Play sound
                try {
                    watchYoutubeVideo(item.getTitle() + " Trailer");
                } catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.youtube"));
                    startActivity(intent);
                }
                break;
            case R.id.btnBack:
                sCancel.start();  // Play sound

                Intent intent = new Intent(DataOfMovie.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}
