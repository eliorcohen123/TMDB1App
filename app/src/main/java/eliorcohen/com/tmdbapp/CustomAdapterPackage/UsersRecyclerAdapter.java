package eliorcohen.com.tmdbapp.CustomAdapterPackage;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eliorcohen.com.tmdbapp.ModelsPackage.UserModel;
import eliorcohen.com.tmdbapp.R;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<UserModel> listUserModels;

    public UsersRecyclerAdapter(List<UserModel> listUserModels) {
        this.listUserModels = listUserModels;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_recycler, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(listUserModels.get(position).getName());
        holder.textViewEmail.setText(listUserModels.get(position).getEmail());
        holder.textViewPassword.setText(listUserModels.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(), "" + listUserModels.size());
        return listUserModels.size();
    }

    // ViewHolder class
    class UserViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewName;
        AppCompatTextView textViewEmail;
        AppCompatTextView textViewPassword;

        UserViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            textViewEmail = view.findViewById(R.id.textViewEmail);
            textViewPassword = view.findViewById(R.id.textViewPassword);
        }
    }

}
