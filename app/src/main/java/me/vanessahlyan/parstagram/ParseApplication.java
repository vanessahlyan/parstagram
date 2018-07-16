package me.vanessahlyan.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import me.vanessahlyan.parstagram.model.Post;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("parseId") 
                .clientKey("vanessa")  
                .clientBuilder(builder)
                .server("https://parstagram-vanessahlyan.herokuapp.com/parse/").build());

        ParseUser user = new ParseUser();
        
        user.setUsername("v");
        user.setPassword("1");
        user.setEmail("email@example.com");
        
        user.put("phone", "650-253-0000");
        
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // proceed to app
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
