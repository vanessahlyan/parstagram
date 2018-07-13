package me.vanessahlyan.parstagram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.vanessahlyan.parstagram.model.Post;

@SuppressLint("NewApi")
public class HomeActivity extends AppCompatActivity implements HomeFragment.TalkToActivity, UserFragment.TalkToActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();

    final HomeFragment fragmentHome = new HomeFragment();
    final UserFragment fragmentUser = new UserFragment();
    final CreateFragment fragmentCreate = new CreateFragment();
    final DetailFragment fragmentDetail = new DetailFragment();

    private Button refreshButton;
    private Button logoutButton;

    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_IMAGE_REQUEST = 1;
    File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragmentHome).commit();

        logoutButton = findViewById(R.id.btnLogout);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction fragmentTransaction;
                    switch (item.getItemId()) {
                        case R.id.btnHome:
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContainer, fragmentHome).commit();
                            return true;

                        case R.id.btnUser:
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContainer, fragmentUser).commit();
                            return true;

                        case R.id.btnUploadPhoto:
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContainer, fragmentCreate).commit();
                            return true;
                        default:
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContainer, fragmentHome).commit();
                            return true;
                    }
                }
            }
        );

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        /*
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        */
    }






    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch(item.getItemId()) {
            case R.id.toolbar_camera:
                launchCamera();
                return true;
            case R.id.toolbar_logout:
                logout();
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }



    public void onCameraClick() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragmentCreate).commit();
        launchCamera();
    }




    public void loadTopPosts(){
                    final Post.Query postsQuery = new Post.Query();
                    postsQuery.getTop().withUser();

                    postsQuery.findInBackground(new FindCallback<Post>() {
                        @Override
                        public void done(List<Post> objects, ParseException e) {
                            if (e == null) {
                for (int i = 0; i < objects.size(); ++i) {
                    Log.d("HomeActivity", "Post[" + i + "] = "
                            + objects.get(i).getDescription()
                            + "\nusername = " + objects.get(i).getUser().getUsername());
                }

            } else {
                e.printStackTrace();
            }
            }
        });

    }


    private void logout(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void launchPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    public void launchCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "me.vanessahlyan.parstagram", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo

            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "HomeActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("HomeActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String imageString = photoFile.getAbsolutePath();
                fragmentCreate.processImageString(imageString);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
                fragmentCreate.processImageBitmap(bitmap, uri);

            }
        }
    }

    @Override
    public void talk(Post post) {
        fragmentDetail.setPost(post);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragmentDetail).commit();

    }

    @Override
    public void userToDetail(Post post) {
        fragmentDetail.setPost(post);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragmentDetail).commit();

    }

    public void goToHome() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragmentHome).commit();
    }
}
