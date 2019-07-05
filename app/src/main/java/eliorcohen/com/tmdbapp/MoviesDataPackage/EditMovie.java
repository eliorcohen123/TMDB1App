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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import eliorcohen.com.tmdbapp.MainAndOtherPackage.MainActivity;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;

public class EditMovie extends AppCompatActivity {

    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app
    private int id;
    private MovieModel item;
    private RadioGroup rg1;
    private RadioButton rb1, rb2;
    private EditText subject, body, URL;
    private Button btnBack;
    private TextView textViewShow, textViewOK;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_movie);

        initUI();
        radioGroup();
        getData();
        btnBack();
    }

    private void initUI() {
        id = getIntent().getExtras().getInt(getString(R.string.movie_id)); // GetSerializable for the ID
        item = (MovieModel) getIntent().getExtras().getSerializable(getString(R.string.movie_edit)); // GetSerializable for the texts

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

        mMovieDBHelper = new MovieDBHelper(EditMovie.this);
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
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    MediaPlayer sRadioButton = MediaPlayer.create(EditMovie.this, R.raw.radiobutton_sound);
                    sRadioButton.start();  // Play sound

                    item.setIs_watch(1);

                    mMovieDBHelper.updateMovieIsWatch(item);
                } else if (checkedId == R.id.radioButton2) {
                    MediaPlayer sRadioButton = MediaPlayer.create(EditMovie.this, R.raw.radiobutton_sound);
                    sRadioButton.start();  // Play sound

                    item.setIs_watch(0);

                    mMovieDBHelper.updateMovieIsWatch(item);
                }
            }
        });
    }

    private void getData() {
        subject.setText(item.getTitle());  // GetSerializable of subject
        body.setText(item.getOverview());  // GetSerializable of body
        URL.setText(item.getPoster_path());  // GetSerializable of URL

        // Button that does the following:
        textViewOK.setOnClickListener(new View.OnClickListener() {
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
                    mMovieDBHelper.updateMovie(title, overview, url, id);

                    // Pass from EditMovie to MainActivity
                    Intent intentEditToMain = new Intent(EditMovie.this, MainActivity.class);
                    startActivity(intentEditToMain);
                }
            }
        });

        //Initialize the ImageView
        String picture = "https://image.tmdb.org/t/p/w154" + item.getPoster_path();
        Picasso.get().load(picture).into(imageView);
        imageView.setVisibility(View.INVISIBLE); //Set the ImageView Invisible

        // Button to show the ImageView
        textViewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sShowImage = MediaPlayer.create(EditMovie.this, R.raw.show_image_sound);
                sShowImage.start();  // Play sound

                URL.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void btnBack() {
        // Button are back to the previous activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sCancel = MediaPlayer.create(EditMovie.this, R.raw.cancel_and_move_sound);
                sCancel.start();  // Play sound

                onBackPressed();
            }
        });
    }

}
