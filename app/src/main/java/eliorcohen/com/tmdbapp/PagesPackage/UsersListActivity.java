package eliorcohen.com.tmdbapp.PagesPackage;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import eliorcohen.com.tmdbapp.DataAppPackage.AuthenticationDBHelper;
import eliorcohen.com.tmdbapp.CustomAdaptersPackage.CustomAdapterUsers;
import eliorcohen.com.tmdbapp.ModelsPackage.UserModel;
import eliorcohen.com.tmdbapp.R;

public class UsersListActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<UserModel> listUserModels;
    private CustomAdapterUsers customAdapterUsers;
    private AuthenticationDBHelper authenticationDBHelper;
    private Button btnBack;
    private MediaPlayer sBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        initUI();
        initListeners();
        showUI();
        initObjects();
    }

    // This method is to initialize views
    private void initUI() {
        textViewName = findViewById(R.id.textViewName);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        btnBack = findViewById(R.id.btnBack);

        sBack = MediaPlayer.create(UsersListActivity.this, R.raw.cancel_and_move_sound);
    }

    private void initListeners() {
        btnBack.setOnClickListener(this);
    }

    private void showUI() {
        getSupportActionBar().setTitle("");
    }

    // This method is to initialize objects to be used
    private void initObjects() {
        listUserModels = new ArrayList<>();
        customAdapterUsers = new CustomAdapterUsers(listUserModels);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(customAdapterUsers);
        authenticationDBHelper = new AuthenticationDBHelper(activity);

        getDataFromSQLite();
    }

    // This method is to fetch all user records from SQLite
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUserModels.clear();
                listUserModels.addAll(authenticationDBHelper.getAllUser());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                customAdapterUsers.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                sBack.start();  // Play sound

                onBackPressed();
                break;
        }
    }

}
