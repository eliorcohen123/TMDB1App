package eliorcohen.com.tmdbapp.CustomAdapterPackage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eliorcohen.com.tmdbapp.DataAppPackage.MovieModel;
import eliorcohen.com.tmdbapp.MoviesDataPackage.AddMovieFromInternet;
import eliorcohen.com.tmdbapp.R;

public class MovieCustomAdapterInternet extends RecyclerView.Adapter<MovieCustomAdapterInternet.CustomViewHolder> {

    private Context context;
    private List<MovieModel> dataList;

    public MovieCustomAdapterInternet(Context context, List<MovieModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_row_total, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.title1.setText(dataList.get(position).getTitle());
        holder.overview1.setText(dataList.get(position).getOverview());
        Picasso.get().load("https://image.tmdb.org/t/p/original" + dataList.get(position).getPoster_path()).into(holder.image1);

        holder.linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearchToAddInternet = new Intent(context, AddMovieFromInternet.class);
                intentSearchToAddInternet.putExtra(context.getString(R.string.movie_add_from_internet), dataList.get(position));
                context.startActivity(intentSearchToAddInternet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
