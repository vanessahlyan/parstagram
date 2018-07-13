package me.vanessahlyan.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vanessahlyan.parstagram.model.Post;

public class DetailFragment extends Fragment {
    Post currentPost;

    @BindView(R.id.ivPhoto) ImageView photo;
    @BindView(R.id.tvTime) TextView time;
    @BindView(R.id.tvDescription) TextView description;
    @BindView(R.id.tvUsername) TextView username;
    @BindView(R.id.ivProfileImage) ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, parent, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        description.setText(currentPost.getDescription());
        username.setText(currentPost.getUser().getUsername());
        time.setText(currentPost.getRelativeTimeAgo());

        Glide.with(getActivity()).load(currentPost.getImage().getUrl()).into(photo);
        Glide.with(getActivity()).load(currentPost.getUser().getParseFile("profileImage").getUrl()).into(profileImage);



    }

    public void setPost(Post post) {
        currentPost = post;
    }


}
