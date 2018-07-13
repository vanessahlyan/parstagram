package me.vanessahlyan.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.vanessahlyan.parstagram.model.Post;

public class UserFragment extends Fragment{
    GridAdapter gridAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    ProgressBar pb;
    private SwipeRefreshLayout swipeContainer;
    CircleImageView profileImage;

    interface TalkToActivity {
        void userToDetail(Post post);
    }

    private UserFragment.TalkToActivity talkToActivity;
    private GridAdapter.Callback callback = new GridAdapter.Callback() {
        @Override
        public void respond(Post post) {
            DetailFragment fragmentB = new DetailFragment();
            fragmentB.setPost(post);
            talkToActivity.userToDetail(post);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserFragment.TalkToActivity) {
            talkToActivity = (UserFragment.TalkToActivity) context;
        } else {
            throw new IllegalStateException("Containing context must implement UserFragment.talkToActivity.");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = (CircleImageView) view.findViewById(R.id.ivProfileImage);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvGrid);


        posts = new ArrayList<>();
        gridAdapter = new GridAdapter(posts, callback);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvPosts.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
        rvPosts.setAdapter(gridAdapter);

    }
}
