package eliorcohen.com.tmdbapp.PagesPackage;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import eliorcohen.com.tmdbapp.ViewModelsPackege.MovieViewModelFavorites;
import eliorcohen.com.tmdbapp.OthersPackage.ConApp;
import eliorcohen.com.tmdbapp.R;

public class DeleteAllData extends AppCompatActivity implements View.OnClickListener {

    private MovieViewModelFavorites movieViewModelFavorites;
    private Button buttonDeleteAll, btnBack;
    private MediaPlayer sDeleteAll, sCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_all_data);

        initUI();
        initListeners();
    }

    private void initUI() {
        buttonDeleteAll = findViewById(R.id.buttonDeleteAll);
        btnBack = findViewById(R.id.btnBack);

        movieViewModelFavorites = new MovieViewModelFavorites(ConApp.getApplication());

        sDeleteAll = MediaPlayer.create(DeleteAllData.this, R.raw.delete_all_sound);
        sCancel = MediaPlayer.create(DeleteAllData.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        buttonDeleteAll.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDeleteAll:
                sDeleteAll.start();  // Play sound

                movieViewModelFavorites.deleteAll();

                Toast toast = Toast.makeText(DeleteAllData.this, "All the data are deleted!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.colorBrown));
                toast.show();

                Intent intentDeleteAllDataToMain = new Intent(DeleteAllData.this, MainActivity.class);
                startActivity(intentDeleteAllDataToMain);
                break;
            case R.id.btnBack:
                sCancel.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
