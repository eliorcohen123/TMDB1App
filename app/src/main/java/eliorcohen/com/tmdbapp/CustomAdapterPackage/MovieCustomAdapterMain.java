package eliorcohen.com.tmdbapp.CustomAdapterPackage;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eliorcohen.com.tmdbapp.ModelsPackage.Results;
import eliorcohen.com.tmdbapp.ViewModelsPackege.MovieViewModelFavorites;
import eliorcohen.com.tmdbapp.OthersPackage.ConApp;
import eliorcohen.com.tmdbapp.PagesPackage.DataOfMovieActivity;
import eliorcohen.com.tmdbapp.PagesPackage.DeleteMovieActivity;
import eliorcohen.com.tmdbapp.PagesPackage.EditMovieActivity;
import eliorcohen.com.tmdbapp.R;

public class MovieCustomAdapterMain extends RecyclerView.Adapter<MovieCustomAdapterMain.CustomViewHolder> {

    private Context context;
    private List<Results> dataList;
    private MovieViewModelFavorites movieViewModelFavorites;
    private MediaPlayer sMove;

    public MovieCustomAdapterMain(Context context, List<Results> dataList) {
        this.context = context;
        this.dataList = dataList;
        sMove = MediaPlayer.create(context, R.raw.cancel_and_move_sound);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final View mView;
        private TextView title1, overview1;
        private ImageView image1;
        private LinearLayout linearLayout1;
        private MediaPlayer sMove, sDelete;

        CustomViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            title1 = mView.findViewById(R.id.title1);
            overview1 = mView.findViewById(R.id.overview1);
            image1 = mView.findViewById(R.id.image1);
            linearLayout1 = mView.findViewById(R.id.linear1);

            sMove = MediaPlayer.create(context, R.raw.cancel_and_move_sound);
            sDelete = MediaPlayer.create(context, R.raw.delete_sound);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem share = menu.add(Menu.NONE, 2, 2, "Share");
            MenuItem delete = menu.add(Menu.NONE, 3, 3, "Delete");

            edit.setOnMenuItemClickListener(onChange);
            share.setOnMenuItemClickListener(onChange);
            delete.setOnMenuItemClickListener(onChange);
        }

        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Results current = dataList.get(getAdapterPosition());
                switch (item.getItemId()) {
                    case 1:
                        sMove.start();  // Play sound

                        Intent intent = new Intent(context, EditMovieActivity.class);
                        intent.putExtra(context.getString(R.string.movie_id), current.getId());
                        intent.putExtra(context.getString(R.string.movie_edit), current);
                        context.startActivity(intent);
                        break;
                    case 2:
                        sMove.start();  // Play sound

                        String title = current.getTitle();
                        String overview = current.getOverview();
                        String URL = "https://image.tmdb.org/t/p/original" + current.getPoster_path();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Title: " + title + "\nOverview: " + overview + "\nURL: " + URL);
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);
                        break;
                    case 3:
                        sDelete.start();  // Play sound

                        movieViewModelFavorites = new MovieViewModelFavorites(ConApp.getApplication());
                        movieViewModelFavorites.deleteMovie(current);

                        Intent intentDeleteData = new Intent(context, DeleteMovieActivity.class);
                        context.startActivity(intentDeleteData);
                        break;
                }
                return false;
            }
        };
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_row_total, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Results current = dataList.get(position);
        holder.title1.setText(current.getTitle());
        holder.overview1.setText(current.getOverview());
        Picasso.get().load("https://image.tmdb.org/t/p/original" + current.getPoster_path()).into(holder.image1);

        holder.linearLayout1.setOnClickListener(v -> {
            sMove.start();  // Play sound

            Intent intent = new Intent(context, DataOfMovieActivity.class);
            intent.putExtra(context.getString(R.string.movie_id), current.getId());
            intent.putExtra(context.getString(R.string.movie_edit), current);
            context.startActivity(intent);
        });

        setFadeAnimation(holder.mView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1500);
        view.startAnimation(anim);
    }

}
