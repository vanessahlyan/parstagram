package me.vanessahlyan.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import me.vanessahlyan.parstagram.model.Post;

public class HomeFragment extends Fragment{
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    ProgressBar pb;
    private SwipeRefreshLayout swipeContainer;

    interface TalkToActivity {
        void talk(Post post);
    }

    private TalkToActivity talkToActivity;
    private PostAdapter.Callback callback = new PostAdapter.Callback() {
        @Override
        public void respond(Post post) {
            DetailFragment fragmentB = new DetailFragment();
            fragmentB.setPost(post);
            talkToActivity.talk(post);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TalkToActivity) {
            talkToActivity = (TalkToActivity) context;
        } else {
            throw new IllegalStateException("Containing context must implement HomeFragment.talkToActivity.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPost);


        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts, callback);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvPosts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvPosts.setAdapter(postAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopPosts();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        loadTopPosts();
    }

    public void loadTopPosts (){

        pb.setVisibility(ProgressBar.VISIBLE);

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    postAdapter.clear();
                    postAdapter.addAll(objects);
                    postAdapter.notifyDataSetChanged();

                    swipeContainer.setRefreshing(false);
                    pb.setVisibility(ProgressBar.INVISIBLE);

                } else {
                    e.printStackTrace();
                }
            }
        });

        pb.setVisibility(ProgressBar.INVISIBLE);

    }

}
