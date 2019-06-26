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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eliorcohen.com.tmdbapp.DataAppPackage.MovieDBHelper;
import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.MoviesDataPackage.DataOfMovie;
import eliorcohen.com.tmdbapp.MoviesDataPackage.DeleteMovie;
import eliorcohen.com.tmdbapp.MoviesDataPackage.EditMovie;
import eliorcohen.com.tmdbapp.R;

public class MovieCustomAdapterMain extends RecyclerView.Adapter<MovieCustomAdapterMain.CustomViewHolder> {

    private Context context;
    private List<MovieModel> dataList;
    private MovieDBHelper mMovieDBHelper;  // The SQLiteHelper of the app

    public MovieCustomAdapterMain(Context context, List<MovieModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final View mView;
        private TextView title1, overview1;
        private ImageView image1;
        private LinearLayout linearLayout1;

        CustomViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            title1 = mView.findViewById(R.id.title1);
            overview1 = mView.findViewById(R.id.overview1);
            image1 = mView.findViewById(R.id.image1);
            linearLayout1 = mView.findViewById(R.id.linear1);

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
                MovieModel current = dataList.get(getAdapterPosition());
                switch (item.getItemId()) {
                    case 1:
                        MediaPlayer sMove = MediaPlayer.create(context, R.raw.cancel_and_move_sound);
                        sMove.start();  // Play sound

                        Intent intent = new Intent(context, EditMovie.class);
                        intent.putExtra(context.getString(R.string.movie_id), current.getId());
                        intent.putExtra(context.getString(R.string.movie_edit), current);
                        context.startActivity(intent);
                        break;
                    case 2:
                        MediaPlayer sSave = MediaPlayer.create(context, R.raw.cancel_and_move_sound);
                        sSave.start();  // Play sound

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
                        MediaPlayer sDelete = MediaPlayer.create(context, R.raw.delete_sound);
                        sDelete.start();  // Play sound

                        mMovieDBHelper = new MovieDBHelper(context);
                        mMovieDBHelper.deleteMovie(current);

                        Intent intentDeleteData = new Intent(context, DeleteMovie.class);
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
        View view = layoutInflater.inflate(R.layout.movie_item_row_internet, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        MovieModel current = dataList.get(position);
        holder.title1.setText(current.getTitle());
        holder.overview1.setText(current.getOverview());
        Picasso.get().load("https://image.tmdb.org/t/p/original" + current.getPoster_path()).into(holder.image1);

        holder.linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer sMove = MediaPlayer.create(context, R.raw.cancel_and_move_sound);
                sMove.start();  // Play sound

                Intent intent = new Intent(context, DataOfMovie.class);
                intent.putExtra(context.getString(R.string.movie_id), dataList.get(position).getId());
                intent.putExtra(context.getString(R.string.movie_edit), dataList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
