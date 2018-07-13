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

import com.bumptech.glide.Glide;

import java.util.List;

import me.vanessahlyan.parstagram.model.Post;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private List<Post> mPosts;
    Context context;


    interface Callback {
        void respond(Post post);
    }

    private GridAdapter.Callback mCallback;


    public GridAdapter(List<Post> posts, GridAdapter.Callback callback) {
        mPosts = posts;
        mCallback = callback;

    }


    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext(); // context is UserFragment
        LayoutInflater inflater = LayoutInflater.from(context);

        View gridView = inflater.inflate(R.layout.card_view_post, parent, false);
        return new ViewHolder(gridView, mCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivImage;
        GridAdapter.Callback mCallback;


        public ViewHolder (@NonNull View itemView, GridAdapter.Callback callback) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
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





    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }


}
