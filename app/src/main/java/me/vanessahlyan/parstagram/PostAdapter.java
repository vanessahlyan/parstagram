package me.vanessahlyan.parstagram;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.vanessahlyan.parstagram.model.Post;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mPosts;
    Context context;


    interface Callback {
        void respond(Post post);
    }

    private Callback mCallback;


    public PostAdapter(List<Post> posts, Callback callback) {
        mPosts = posts;
        mCallback = callback;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext(); // context is HomeFragment
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView, mCallback);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivPhoto;
        public TextView tvUsername;
        public TextView tvDescription;
        public CircleImageView ivProfileImage;
        public TextView tvTime;

        Callback mCallback;


        public ViewHolder (@NonNull View itemView, Callback callback) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            ivProfileImage = (CircleImageView) itemView.findViewById(R.id.ivProfileImage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(this);
            mCallback = callback;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post currentPost = mPosts.get(position);
                mCallback.respond(currentPost);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.tvDescription.setText(post.getDescription());
        holder.tvTime.setText(post.getRelativeTimeAgo());


        try {
            ParseUser temp = post.getUser().fetchIfNeeded();
            holder.tvUsername.setText("@" + temp.getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivPhoto);
        Glide.with(context).load(post.getUser().getParseFile("profileImage").getUrl()).into(holder.ivProfileImage);




    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }



}
