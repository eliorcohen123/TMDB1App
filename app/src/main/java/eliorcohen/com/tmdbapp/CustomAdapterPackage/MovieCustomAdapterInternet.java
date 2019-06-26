package eliorcohen.com.tmdbapp.CustomAdapterPackage;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.R;

public class MovieCustomAdapterInternet extends ArrayAdapter<MovieModel> {

    private Context mContext;  //Context
    private ArrayList<MovieModel> mMovieList;  // ArrayList of MovieModel

    public MovieCustomAdapterInternet(Context context_, ArrayList<MovieModel> movie_) {
        super(context_, 0, movie_);
        mContext = context_;
        mMovieList = movie_;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.movie_item_row_internet, parent, false);
        }

        MovieModel currentMovie = getItem(position);  // Position of items

        // Put the image in image1
        if (currentMovie != null) {  // If the position of the items not null
            ImageView image1 = listItem.findViewById(R.id.image1);
            assert currentMovie != null;  // If the position of the item not null in the ImageView
            Picasso.get().load("https://image.tmdb.org/t/p/original" + currentMovie.getPoster_path()).into(image1);

            // Put the text in title1
            TextView title1 = listItem.findViewById(R.id.title1);
            title1.setText(String.valueOf(currentMovie.getTitle()));

//            // Put the text in vote_average1
//            TextView vote_average1 = listItem.findViewById(R.id.vote_average1);
//            vote_average1.setText("Vote average: " + String.valueOf(currentMovie.getVote_average()));
//
//            // Put the text in popularity1
//            TextView popularity1 = listItem.findViewById(R.id.popularity1);
//            popularity1.setText("Popularity: " + String.valueOf(currentMovie.getPopularity()));
//
//            // Put the text in release_date1
//            TextView release_date1 = listItem.findViewById(R.id.release_date1);
//            release_date1.setText("Release date: " + String.valueOf(currentMovie.getRelease_date()));
//
//            // Put the text in vote_count1
//            TextView vote_count1 = listItem.findViewById(R.id.vote_count1);
//            vote_count1.setText("Vote count: " + String.valueOf(currentMovie.getVote_count()));

            // Put the text in overview1
            TextView overview1 = listItem.findViewById(R.id.overview1);
            overview1.setText(String.valueOf(currentMovie.getOverview()));

//            TextView original_title1 = listItem.findViewById(R.id.original_title1);
//            original_title1.setText("\n" + "\n" + "Original title: " + String.valueOf(currentMovie.getOriginal_title()));
//
//            TextView original_language1 = listItem.findViewById(R.id.original_language1);
//            original_language1.setText("\n" + "Original language: " + String.valueOf(currentMovie.getOriginal_language()));
//
//            TextView id1 = listItem.findViewById(R.id.id1);
//            id1.setText("\n" + "Id: " + String.valueOf(currentMovie.getId()));
//
//            TextView video1 = listItem.findViewById(R.id.video1);
//            video1.setText("\n" + "Have a video? " + String.valueOf(currentMovie.isVideo()));
//
//            TextView adult1 = listItem.findViewById(R.id.adult1);
//            adult1.setText("\n" + "For adults? " + String.valueOf(currentMovie.isAdult()));
//
//            ImageView image2 = listItem.findViewById(R.id.image2);
//            Picasso.get().load("https://image.tmdb.org/t/p/original" + currentMovie.getBackdrop_path()).into(image2);
        }
        return listItem;
    }

}
