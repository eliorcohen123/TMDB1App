package eliorcohen.com.tmdbapp.PagesPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import eliorcohen.com.tmdbapp.ViewModelsPackege.MovieViewModelFavorites;
import eliorcohen.com.tmdbapp.OthersPackage.ConApp;
import eliorcohen.com.tmdbapp.ModelsPackage.Results;
import eliorcohen.com.tmdbapp.R;

public class EditMovie extends AppCompatActivity implements View.OnClickListener {

    private MovieViewModelFavorites movieViewModelFavorites;
    private int id;
    private Results item;
    private RadioGroup rg1;
    private RadioButton rb1, rb2;
    private EditText subject, body, URL;
    private TextView textViewShow, textViewOK;
    private Button btnBack;
    private ImageView imageView;
    private MediaPlayer sRadioButton, sError, sAdd, sShowImage, sCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_movie);

        initUI();
        initListeners();
        radioGroup();
        getData();
    }

    private void initUI() {
        id = getIntent().getExtras().getInt(getString(R.string.movie_id)); // GetSerializable for the ID
        item = (Results) getIntent().getExtras().getSerializable(getString(R.string.movie_edit)); // GetSerializable for the texts

        rg1 = findViewById(R.id.radioGroup);  // ID of the RadioGroup of EditMovie
        rb1 = findViewById(R.id.radioButton1);  // ID of the RadioButton1 of EditMovie
        rb2 = findViewById(R.id.radioButton2);  // ID of the RadioButton2 of EditMovie

        subject = findViewById(R.id.editTextSubject);  // ID of the subject
        body = findViewById(R.id.editTextBody);  // ID of the body
        URL = findViewById(R.id.editTextURL);  // ID of the URL

        textViewOK = findViewById(R.id.textViewOK);
        textViewShow = findViewById(R.id.textViewShow);

        btnBack = findViewById(R.id.btnBack);

        imageView = findViewById(R.id.imageView);

        movieViewModelFavorites = new MovieViewModelFavorites(ConApp.getApplication());

        sRadioButton = MediaPlayer.create(EditMovie.this, R.raw.radiobutton_sound);
        sError = MediaPlayer.create(EditMovie.this, R.raw.error_sound);
        sAdd = MediaPlayer.create(EditMovie.this, R.raw.add_and_edit_sound);
        sShowImage = MediaPlayer.create(EditMovie.this, R.raw.show_image_sound);
        sCancel = MediaPlayer.create(EditMovie.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        textViewShow.setOnClickListener(this);
        textViewOK.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void radioGroup() {
        // Checked if the RadioButton equal to 1 or 2
        assert item != null;
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
            movieViewModelFavorites.updateMovieIsWatch(item);
        });
    }

    private void getData() {
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

                if (TextUtils.isEmpty(subject.getText())) {  // If the text are empty the movie will not be approved
                    sError.start();  // Play sound

                    subject.setError("Title is required!");  // Print text of error if the text are empty
                } else {
                    sAdd.start();  // Play sound

                    // The texts in the SQLiteHelper
                    movieViewModelFavorites.updateMovie(title, overview, url, id);

                    // Pass from EditMovie to MainActivity
                    Intent intentEditToMain = new Intent(EditMovie.this, MainActivity.class);
                    startActivity(intentEditToMain);
                }
                break;
            case R.id.textViewShow:
                sShowImage.start();  // Play sound

                URL.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
            case R.id.btnBack:
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
