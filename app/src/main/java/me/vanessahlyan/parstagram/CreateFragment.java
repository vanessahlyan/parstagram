package me.vanessahlyan.parstagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vanessahlyan.parstagram.model.Post;

public class CreateFragment extends Fragment {
    @BindView(R.id.etDescription) EditText descriptionInput;
    @BindView(R.id.tvUsername) TextView username;
    @BindView(R.id.ivPreview) ImageView preview;
    @BindView(R.id.btnPost) Button postButton;
    @BindView(R.id.btnTakePhoto) Button takePhotoButton;
    @BindView(R.id.btnUploadPhoto) Button uploadPhotoButton;
    @BindView(R.id.pbLoadingCreate) ProgressBar createProgressBar;

    File photoFile;
    Bitmap photoBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProgressBar.setVisibility(ProgressBar.VISIBLE);
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photoBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                final ParseFile parseFile = new ParseFile(bytes.toByteArray());

                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            createPost(description, parseFile, user);
                            Toast.makeText(getActivity(), "Created post", Toast.LENGTH_LONG).show();
                            createProgressBar.setVisibility(ProgressBar.INVISIBLE);
                            //HomeActivity activity = (HomeActivity) getActivity();
                            //activity.goToHome();
                        }
                        else {
                            Toast.makeText(getActivity(), "cannot create post", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.launchCamera();
            }
        });

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.launchPhotos();
            }
        });

    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post success");
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    // method that takes a string file path
    public void processImageString(String uri) {
        Bitmap takenImage = BitmapFactory.decodeFile(uri);
        photoFile = new File(uri);
        Glide.with(getActivity()).load(takenImage).into(preview);
    }

    public void processImageBitmap(Bitmap takenImage, Uri uri) {
        photoBitmap = takenImage;
        Glide.with(getActivity()).load(takenImage).into(preview);
    }






}
